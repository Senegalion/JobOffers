package pl.joboffers.http.offer;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.springframework.web.client.RestTemplate;
import pl.joboffers.domain.offer.OfferFetchable;
import pl.joboffers.infrastructure.offer.http.OfferHttpClient;
import pl.joboffers.infrastructure.offer.http.OfferHttpClientConfig;

public class OfferHttpClientConfigurationIntegrationTest extends OfferHttpClientConfig {
    public static final String WIRE_MOCK_HOST = "http://localhost";

    private final WireMockExtension wireMockServer;

    public OfferHttpClientConfigurationIntegrationTest(WireMockExtension wireMockServer) {
        this.wireMockServer = wireMockServer;
    }

    public OfferFetchable remoteOfferFetcherClient() {
        RestTemplate restTemplate = restTemplate(1000, 1000, restTemplateResponseErrorHandler());
        return new OfferHttpClient(restTemplate, WIRE_MOCK_HOST, wireMockServer.getPort());
    }
}
