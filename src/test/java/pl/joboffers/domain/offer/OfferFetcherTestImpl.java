package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponseDto;

import java.util.List;

public class OfferFetcherTestImpl implements OfferFetchable {
    List<JobOfferResponseDto> listOfOffers;

    public OfferFetcherTestImpl(List<JobOfferResponseDto> listOfOffers) {
        this.listOfOffers = listOfOffers;
    }

    @Override
    public List<JobOfferResponseDto> fetchOffers() {
        return listOfOffers;
    }
}
