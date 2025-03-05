package pl.joboffers.domain.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class OfferFacadeConfiguration {
    @Bean
    OfferFacade offerFacade(
            OfferFetchable offerFetchable
    ) {
        OfferRepository offerRepository = new OfferRepository() {
            Map<String, Offer> offers = new ConcurrentHashMap<>();

            @Override
            public List<Offer> findAll() {
                return offers
                        .values()
                        .stream()
                        .toList();
            }

            @Override
            public Optional<Offer> findById(String id) {
                return Optional.ofNullable(offers.get(id));
            }

            @Override
            public Offer save(Offer offer) {
                String offerId = UUID.randomUUID().toString();
                Offer offerToBeSaved = Offer.builder()
                        .id(offerId)
                        .companyName(offer.companyName())
                        .position(offer.position())
                        .salary(offer.salary())
                        .offerUrl(offer.offerUrl())
                        .build();
                offers.put(offerToBeSaved.id(), offerToBeSaved);
                return offerToBeSaved;
            }

            @Override
            public boolean existsByOfferUrl(Offer offer) {
                List<String> retrievedOfferUrls = offers.values().stream()
                        .map(Offer::offerUrl)
                        .filter(offerUrl -> offerUrl.equals(offer.offerUrl()))
                        .toList();
                return !retrievedOfferUrls.isEmpty();
            }

            @Override
            public List<Offer> saveAll(List<Offer> newOffers) {
                return newOffers.stream()
                        .map(this::save)
                        .toList();
            }
        };

        OfferService offerService = new OfferService(offerFetchable, offerRepository);
        return new OfferFacade(offerRepository, offerService);
    }
}
