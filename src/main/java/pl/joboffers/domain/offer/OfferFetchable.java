package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponseDto;

import java.util.List;

public interface OfferFetchable {
    List<JobOfferResponseDto> fetchOffers();
}
