package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponseDto;

import java.util.List;

@FunctionalInterface
public interface OfferFetchable {
    List<JobOfferResponseDto> fetchOffers();
}
