package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class OfferService {
    private final OfferFetchable offerFetcher;
    private final OfferRepository offerRepository;
    public List<Offer> fetchAllAndSaveAllIfNotExists() {
        return null;
    }
}
