package pl.joboffers.http.offer;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.server.ResponseStatusException;
import pl.joboffers.SampleJobOfferResponse;
import pl.joboffers.domain.offer.OfferFetchable;
import wiremock.org.apache.hc.core5.http.HttpStatus;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class OfferHttpClientErrorsIntegrationTest implements SampleJobOfferResponse {
    public static final String WIRE_MOCK_HOST = "http://localhost";
    public static final int CONNECTION_TIMEOUT = 5000;
    public static final int READ_TIMEOUT = 5000;
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String INTERNAL_SERVER_ERROR = "500 INTERNAL_SERVER_ERROR";
    public static final String NO_CONTENT = "204 NO_CONTENT";
    public static final String NOT_FOUND = "404 NOT_FOUND";
    public static final String UNAUTHORIZED = "401 UNAUTHORIZED";
    public static final String OFFERS = "/offers";
    private OfferFetchable offerFetcher;

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @BeforeEach
    void setUp() {
        offerFetcher = new OfferHttpClientConfigurationIntegrationTest(wireMockServer)
                .remoteOfferFetcherClient();
    }

    @Test
    void should_return_500_internal_server_error_when_fault_connection_reset_by_peer() {
        // given
        wireMockServer.stubFor(WireMock.get(OFFERS)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));

        // when
        Throwable throwable = catchThrowable(() -> offerFetcher
                .fetchOffers());

        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo(INTERNAL_SERVER_ERROR);
    }

    @Test
    void should_return_500_internal_server_error_when_fault_empty_response() {
        // given
        wireMockServer.stubFor(WireMock.get(OFFERS)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withFault(Fault.EMPTY_RESPONSE)));

        // when
        Throwable throwable = catchThrowable(() -> offerFetcher.fetchOffers());

        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo(INTERNAL_SERVER_ERROR);
    }

    @Test
    void should_return_500_internal_server_error_when_fault_malformed_response_chunk() {
        // given
        wireMockServer.stubFor(WireMock.get(OFFERS)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
        // when
        Throwable throwable = catchThrowable(() -> offerFetcher.fetchOffers());

        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo(INTERNAL_SERVER_ERROR);
    }

    @Test
    void should_return_500_internal_server_error_when_fault_random_data_then_close() {
        // given
        wireMockServer.stubFor(WireMock.get(OFFERS)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withFault(Fault.RANDOM_DATA_THEN_CLOSE)));

        // when
        Throwable throwable = catchThrowable(() -> offerFetcher.fetchOffers());

        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo(INTERNAL_SERVER_ERROR);
    }

    @Test
    void should_return_zero_job_offers_when_status_is_204_no_content() {
        // given
        wireMockServer.stubFor(WireMock.get(OFFERS)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.SC_NO_CONTENT)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody(bodyWithZeroOffersJson())));

        // when
        Throwable throwable = catchThrowable(() -> offerFetcher.fetchOffers());

        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo(NO_CONTENT);
    }

    @Test
    void should_return_zero_job_offers_when_response_delay_is_5000_ms_and_client_has_1000ms_read_timeout() {
        // given
        wireMockServer.stubFor(WireMock.get(OFFERS)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody(bodyWithZeroOffersJson())
                        .withFixedDelay(5000)));

        // when
        Throwable throwable = catchThrowable(() -> offerFetcher.fetchOffers());

        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo(INTERNAL_SERVER_ERROR);
    }

    @Test
    void should_return_response_not_found_status_exception_when_http_service_returning_not_found_status() {
        // given
        wireMockServer.stubFor(WireMock.get(OFFERS)
                .willReturn(WireMock.aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withStatus(HttpStatus.SC_NOT_FOUND))
        );

        // when
        Throwable throwable = catchThrowable(() -> offerFetcher.fetchOffers());

        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo(NOT_FOUND);
    }

    @Test
    void should_return_response_unauthorized_status_exception_when_http_service_returning_unauthorized_status() {
        // given
        wireMockServer.stubFor(WireMock.get(OFFERS)
                .willReturn(WireMock.aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withStatus(HttpStatus.SC_UNAUTHORIZED))
        );

        // when
        Throwable throwable = catchThrowable(() -> offerFetcher.fetchOffers());

        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo(UNAUTHORIZED);
    }
}
