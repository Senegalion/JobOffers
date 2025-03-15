package pl.joboffers.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.SampleJobOfferResponse;
import pl.joboffers.domain.offer.dto.OfferResponseDto;
import pl.joboffers.infrastructure.offer.scheduler.OfferFetcherScheduler;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserSeeOffersAndAddNewOnesIntegrationTest extends BaseIntegrationTest implements SampleJobOfferResponse {
    @Container
    public static final MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
    public static final String TOKEN = "/token";

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("offer.http.client.config.uri", () -> WIRE_MOCK_HOST);
        registry.add("offer.http.client.config.port", () -> wireMockServer.getPort());
    }

    @Autowired
    OfferFetcherScheduler offerFetcherScheduler;
    private static final String OFFERS = "/offers";

    @Test
    public void userWantToSeeOffersButHaveToBeLoggedInAndExternalServerShouldHaveSomeOffers() throws Exception {
        // step 1: there are no offers in external HTTP server
        // given & when & then
        wireMockServer.stubFor(WireMock.get(OFFERS)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithZeroOffersJson())));

        // step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        // given
        // when
        List<OfferResponseDto> offerResponseDtos = offerFetcherScheduler.fetchAllOffersAndSaveAllIfNotExists();

        // then
        assertThat(offerResponseDtos).isEmpty();

        // step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
        // given
        // when
        ResultActions failedPerformToGetJWTToken = mockMvc.perform(post(TOKEN)
                .content("""
                        {
                        "username" : "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        failedPerformToGetJWTToken
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("""
                        {
                        "message" : "Bad credentials",
                        "status": "UNAUTHORIZED"
                        }
                        """.trim()
                ));

        // step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
        mockMvc.perform(get(OFFERS).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        // step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
        // step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
        // step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get(OFFERS)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        List<OfferResponseDto> offerResponses = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, OfferResponseDto.class));
        assertThat(offerResponses).isEmpty();

        // step 8: there are 2 new offers in external HTTP server
        // given & when & then
        wireMockServer.stubFor(WireMock.get(OFFERS)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithTwoOffersJson())));

        // step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
        // given
        // when
        List<OfferResponseDto> offerResponseDtosWithTwoOffers = offerFetcherScheduler.fetchAllOffersAndSaveAllIfNotExists();

        // then
        assertThat(offerResponseDtosWithTwoOffers).hasSize(2);
        assertAll(
                () -> assertThat(offerResponseDtosWithTwoOffers.get(0).id()).isNotNull(),
                () -> assertThat(offerResponseDtosWithTwoOffers.get(0).companyName()).isEqualTo("Software Engineer - Mobile (m/f/d)"),
                () -> assertThat(offerResponseDtosWithTwoOffers.get(0).position()).isEqualTo("Cybersource"),
                () -> assertThat(offerResponseDtosWithTwoOffers.get(0).salary()).isEqualTo("4k - 8k PLN"),
                () -> assertThat(offerResponseDtosWithTwoOffers.get(0).offerUrl()).isEqualTo("https://nofluffjobs.com/pl/job/software-engineer-mobile-m-f-d-cybersource-poznan-entavdpn"),

                () -> assertThat(offerResponseDtosWithTwoOffers.get(1).id()).isNotNull(),
                () -> assertThat(offerResponseDtosWithTwoOffers.get(1).companyName()).isEqualTo("Junior DevOps Engineer"),
                () -> assertThat(offerResponseDtosWithTwoOffers.get(1).position()).isEqualTo("CDQ Poland"),
                () -> assertThat(offerResponseDtosWithTwoOffers.get(1).salary()).isEqualTo("8k - 14k PLN"),
                () -> assertThat(offerResponseDtosWithTwoOffers.get(1).offerUrl()).isEqualTo("https://nofluffjobs.com/pl/job/junior-devops-engineer-cdq-poland-wroclaw-gnymtxqd")
        );

        // step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000
        // given
        // when
        ResultActions resultActionsForGettingNewlyAddedOffers = mockMvc
                .perform(get(OFFERS)
                        .contentType(MediaType.APPLICATION_JSON)
                );

        // then
        MvcResult mvcResultForGettingNewlyAddedOffers = resultActionsForGettingNewlyAddedOffers.andExpect(status().isOk()).andReturn();
        String jsonForGettingOfferNewlyAddedOffers = mvcResultForGettingNewlyAddedOffers.getResponse().getContentAsString();
        List<OfferResponseDto> retrievedNewlyAddedOffers = objectMapper.readValue(
                jsonForGettingOfferNewlyAddedOffers,
                objectMapper.getTypeFactory().constructCollectionType(List.class, OfferResponseDto.class));

        assertThat(retrievedNewlyAddedOffers).hasSize(2);
        OfferResponseDto firstOffer = retrievedNewlyAddedOffers.get(0);
        OfferResponseDto secondOffer = retrievedNewlyAddedOffers.get(1);

        assertAll(
                () -> {
                    assertThat(firstOffer).isEqualTo(OfferResponseDto.builder()
                            .id(firstOffer.id())
                            .companyName(firstOffer.companyName())
                            .position(firstOffer.position())
                            .salary(firstOffer.salary())
                            .offerUrl(firstOffer.offerUrl())
                            .build());
                },
                () -> assertThat(secondOffer).isEqualTo(OfferResponseDto.builder()
                        .id(secondOffer.id())
                        .companyName(secondOffer.companyName())
                        .position(secondOffer.position())
                        .salary(secondOffer.salary())
                        .offerUrl(secondOffer.offerUrl())
                        .build())
        );

        // step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        //given
        String nonExistingOfferId = "9999";

        // when
        ResultActions resultActionsForGettingOfferWithNonExistingId = mockMvc
                .perform(get(OFFERS + "/" + nonExistingOfferId)
                        .contentType(MediaType.APPLICATION_JSON)
                );

        // then
        resultActionsForGettingOfferWithNonExistingId.andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                        "message" : "Offer with id: [9999] not found",
                        "status": "NOT_FOUND"
                        }
                        """.trim()
                ));

        // step 12: user made GET /offers/1000 and system returned OK(200) with offer
        // given
        String firstOfferId = firstOffer.id();

        // when
        ResultActions performForGettingNewOffer = mockMvc.perform(get(OFFERS + "/" + firstOfferId).contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult resultForGettingNewOffer = performForGettingNewOffer.andExpect(status().isOk()).andReturn();
        String jsonForGettingNewOffer = resultForGettingNewOffer.getResponse().getContentAsString();
        OfferResponseDto newOffer = objectMapper.readValue(jsonForGettingNewOffer, OfferResponseDto.class);
        assertThat(newOffer).isEqualTo(firstOffer);

        // step 13: there are 2 new offers in external HTTP server
        // given & when & then
        wireMockServer.stubFor(WireMock.get(OFFERS)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithFourOffersJson())));

        // step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
        // given
        // when
        List<OfferResponseDto> nextToOffers = offerFetcherScheduler.fetchAllOffersAndSaveAllIfNotExists();

        // then
        assertThat(nextToOffers).hasSize(2);
        assertAll(
                () -> assertThat(nextToOffers.get(0).id()).isNotNull(),
                () -> assertThat(nextToOffers.get(0).companyName()).isEqualTo("Junior Java Developer"),
                () -> assertThat(nextToOffers.get(0).position()).isEqualTo("Sollers Consulting"),
                () -> assertThat(nextToOffers.get(0).salary()).isEqualTo("7 500 - 11 500 PLN"),
                () -> assertThat(nextToOffers.get(0).offerUrl()).isEqualTo("https://nofluffjobs.com/pl/job/junior-java-developer-sollers-consulting-warszawa-s6et1ucc"),

                () -> assertThat(nextToOffers.get(1).id()).isNotNull(),
                () -> assertThat(nextToOffers.get(1).companyName()).isEqualTo("Junior Full Stack Developer"),
                () -> assertThat(nextToOffers.get(1).position()).isEqualTo("Vertabelo S.A."),
                () -> assertThat(nextToOffers.get(1).salary()).isEqualTo("7 000 - 9 000 PLN"),
                () -> assertThat(nextToOffers.get(1).offerUrl()).isEqualTo("https://nofluffjobs.com/pl/job/junior-full-stack-developer-vertabelo-remote-k7m9xpnm")
        );

        // step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000
        // given
        // when
        ResultActions resultActionsForGettingFourOffers = mockMvc.perform(get(OFFERS)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult resultForGettingFourOffers = resultActionsForGettingFourOffers.andExpect(status().isOk()).andReturn();
        String jsonForGettingFourOffer = resultForGettingFourOffers.getResponse().getContentAsString();
        List<OfferResponseDto> retrievedFourOffers = objectMapper.readValue(
                jsonForGettingFourOffer,
                objectMapper.getTypeFactory().constructCollectionType(List.class, OfferResponseDto.class));

        assertThat(retrievedFourOffers).hasSize(4);
        OfferResponseDto expectedThirdOffer = nextToOffers.get(0);
        OfferResponseDto expectedFourthOffer = nextToOffers.get(1);
        assertThat(retrievedFourOffers).contains(
                new OfferResponseDto(
                        expectedThirdOffer.id(),
                        expectedThirdOffer.companyName(),
                        expectedThirdOffer.position(),
                        expectedThirdOffer.salary(),
                        expectedThirdOffer.offerUrl()
                ),
                new OfferResponseDto(
                        expectedFourthOffer.id(),
                        expectedFourthOffer.companyName(),
                        expectedFourthOffer.position(),
                        expectedFourthOffer.salary(),
                        expectedFourthOffer.offerUrl()
                )
        );

        // step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer as body and system returned CREATED(201) with saved offer
        // given
        // when
        ResultActions resultActionsForAddingNewOffer = mockMvc
                .perform(post(OFFERS)
                        .content("""
                                {
                                    "companyName":"testCompany",
                                    "position":"testPosition",
                                    "salary":"5000 - 8000 PLN",
                                    "offerUrl":"https://joboffers/testOffers/1"
                                }
                                """.trim()
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                );

        // then
        MvcResult mvcResultForAddingNewOffer = resultActionsForAddingNewOffer.andExpect(status().isCreated()).andReturn();
        String jsonForAddingNewOffer = mvcResultForAddingNewOffer.getResponse().getContentAsString();
        OfferResponseDto offerResponseDto = objectMapper.readValue(jsonForAddingNewOffer, OfferResponseDto.class);

        assertAll(
                () -> assertThat(offerResponseDto.id()).isNotNull(),
                () -> assertThat(offerResponseDto.companyName()).isEqualTo("testCompany"),
                () -> assertThat(offerResponseDto.position()).isEqualTo("testPosition"),
                () -> assertThat(offerResponseDto.salary()).isEqualTo("5000 - 8000 PLN"),
                () -> assertThat(offerResponseDto.offerUrl()).isEqualTo("https://joboffers/testOffers/1")
        );

        // step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 5 offers
        // given
        String newlyAddedOfferId = offerResponseDto.id();

        // when
        ResultActions resultActionsForGettingOffer = mockMvc.perform(get(OFFERS)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult mvcResultForGettingOffer = resultActionsForGettingOffer.andExpect(status().isOk()).andReturn();
        String jsonForGettingOffer = mvcResultForGettingOffer.getResponse().getContentAsString();
        List<OfferResponseDto> retrievedOffers = objectMapper.readValue(
                jsonForGettingOffer,
                objectMapper.getTypeFactory().constructCollectionType(List.class, OfferResponseDto.class));
        assertAll(
                () -> assertThat(retrievedOffers).hasSize(5),
                () -> {
                    assert retrievedOffers != null;
                    assertThat(retrievedOffers.stream().map(OfferResponseDto::id).toList()).contains(newlyAddedOfferId);
                }
        );
    }
}
