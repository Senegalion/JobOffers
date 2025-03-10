package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class OfferService {
    private final OfferFetchable offerFetcher;
    private final OfferRepository offerRepository;

    public List<Offer> fetchAllAndSaveAllIfNotExists() {
        List<Offer> offers = fetchOffers();
        final List<Offer> newOffers = filterNotExistingOffers(offers);
        return offerRepository.saveAll(newOffers);
    }

    private List<Offer> filterNotExistingOffers(List<Offer> offers) {
        return offers.stream()
                .filter(offer -> !offer.offerUrl().isEmpty())
                .filter(offer -> !offerRepository.existsByOfferUrl(offer.offerUrl()))
                .toList();
    }

    private List<Offer> fetchOffers() {
        return offerFetcher.fetchOffers()
                .stream()
                .map(OfferMapper::mapFromJobOfferResponseToOffer)
                .toList();
    }
}
