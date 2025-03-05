package pl.joboffers.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.JobOffersApplication;
import pl.joboffers.domain.offer.OfferFetchable;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = JobOffersApplication.class, properties = "scheduling.enabled=true")
public class HttpOffersSchedulerTest extends BaseIntegrationTest {
    public static final int TIMEOUT = 2;
    public static final int WANTED_NUMBER_OF_INVOCATIONS = 2;
    @SpyBean
    OfferFetchable remoteOfferClient;

    @Test
    public void shouldReturnHttpClientOffersFetchingExactlyGivenTimes() {
        await().atMost(TIMEOUT, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(remoteOfferClient, times(WANTED_NUMBER_OF_INVOCATIONS)).fetchOffers());
    }
}
