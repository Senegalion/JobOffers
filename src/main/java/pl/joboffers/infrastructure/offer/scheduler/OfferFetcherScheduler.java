package pl.joboffers.infrastructure.offer.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.dto.OfferResponseDto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class OfferFetcherScheduler {
    private final OfferFacade offerFacade;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    public static final String STARTED_OFFERS_FETCHING_MESSAGE = "Started offers fetching {}";
    public static final String STOPPED_OFFERS_FETCHING_MESSAGE = "Stopped offers fetching {}";
    public static final String ADDED_NEW_OFFERS_MESSAGE = "Added new {} offers";

    @Scheduled(fixedDelayString = "${offer.http.scheduler.request.delay}")
    public List<OfferResponseDto> fetchAllOffersAndSaveAllIfNotExists() {
        log.info(STARTED_OFFERS_FETCHING_MESSAGE, dateFormat.format(new Date()));
        List<OfferResponseDto> offerResponseDto = offerFacade.fetchAllOffersAndSaveAllIfNotExists();
        log.info(ADDED_NEW_OFFERS_MESSAGE, offerResponseDto);
        log.info(STOPPED_OFFERS_FETCHING_MESSAGE, dateFormat.format(new Date()));
        return offerResponseDto;
    }
}
