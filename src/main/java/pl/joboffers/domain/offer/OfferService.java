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
        try {
            return offerRepository.saveAll(newOffers);
        } catch (OfferDuplicateException duplicateKeyException) {
            throw new OfferSavingException(duplicateKeyException.getMessage(), offers);
        }
    }

    private List<Offer> filterNotExistingOffers(List<Offer> offers) {
        return offers.stream()
                .filter(offer -> !offer.offerUrl().isEmpty())
                .filter(offer -> !offerRepository.existsByOfferUrl(offer))
                .toList();
    }

    private List<Offer> fetchOffers() {
        return offerFetcher.fetchOffers()
                .stream()
                .map(OfferMapper::mapFromJobOfferResponseToOffer)
                .toList();
    }
}
