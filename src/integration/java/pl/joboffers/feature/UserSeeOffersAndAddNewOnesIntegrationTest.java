package pl.joboffers.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
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
    @Autowired
    OfferFetcherScheduler offerFetcherScheduler;

    @Test
    public void userWantToSeeOffersButHaveToBeLoggedInAndExternalServerShouldHaveSomeOffers() throws Exception {
        // step 1: there are no offers in external HTTP server
        String offersUrl = "/offers";

        wireMockServer.stubFor(WireMock.get(offersUrl)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithZeroOffersJson())));

        // step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        List<OfferResponseDto> offerResponseDtos = offerFetcherScheduler.fetchAllOffersAndSaveAllIfNotExists();
        assertThat(offerResponseDtos).isEmpty();

        // step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
        // step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
        // step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
        // step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
        // step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
        // given

        // when
        ResultActions resultActions = mockMvc.perform(get(offersUrl)
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
        // step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
        // step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000
        // step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        //given
        String nonExistingOfferId = "9999";

        // when
        ResultActions resultActionsForGettingOfferWithNonExistingId = mockMvc
                .perform(get(offersUrl + "/" + nonExistingOfferId)
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
        // step 13: there are 2 new offers in external HTTP server
        // step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
        // step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000
        // step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer as body and system returned CREATED(201) with saved offer
        // given
        // when
        ResultActions resultActionsForAddingNewOffer = mockMvc
                .perform(post(offersUrl)
                        .content("""
                                {
                                    "companyName":"testCompany",
                                    "position":"testPosition",
                                    "salary":"5000 - 8000 PLN",
                                    "offerUrl":"https://joboffers/testOffers/1"
                                }
                                """.trim()
                        )
                        .contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
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

        // step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 1 offer
        // given
        // when
        String newlyAddedOfferId = offerResponseDto.id();
        ResultActions resultActionsForGettingOffer = mockMvc.perform(get(offersUrl)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult mvcResultForGettingOffer = resultActionsForGettingOffer.andExpect(status().isOk()).andReturn();
        String jsonForGettingOffer = mvcResultForGettingOffer.getResponse().getContentAsString();
        List<OfferResponseDto> retrievedOffers = objectMapper.readValue(
                jsonForGettingOffer,
                objectMapper.getTypeFactory().constructCollectionType(List.class, OfferResponseDto.class));
        assertAll(
                () -> assertThat(retrievedOffers).hasSize(1),
                () -> {
                    assert retrievedOffers != null;
                    assertThat(retrievedOffers.stream().map(OfferResponseDto::id).toList()).contains(newlyAddedOfferId);
                }
        );
    }
}
