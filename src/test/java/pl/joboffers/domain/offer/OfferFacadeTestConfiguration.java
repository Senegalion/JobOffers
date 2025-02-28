package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponseDto;

import java.util.List;

public class OfferFacadeTestConfiguration {
    private final OfferFetcherTestImpl offerFetcher;
    private final OfferRepositoryTestImpl offerRepository;

    public OfferFacadeTestConfiguration() {
        this.offerFetcher = new OfferFetcherTestImpl(
                JobOffersData.fetchAllNewJobOffers()
        );
        this.offerRepository = new OfferRepositoryTestImpl();
    }

    OfferFacadeTestConfiguration(List<JobOfferResponseDto> remoteClientOffers) {
        this.offerFetcher = new OfferFetcherTestImpl(remoteClientOffers);
        this.offerRepository = new OfferRepositoryTestImpl();
    }

    OfferFacade offerFacadeForTests() {
        return new OfferFacade(offerRepository, new OfferService(offerFetcher, offerRepository));
    }
}
