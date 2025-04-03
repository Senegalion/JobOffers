package pl.joboffers.domain.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OfferFacadeConfiguration {
    @Bean
    OfferFacade offerFacade(
            OfferFetchable offerFetchable,
            OfferRepository offerRepository
    ) {
        OfferService offerService = new OfferService(offerFetchable, offerRepository);
        return new OfferFacade(offerRepository, offerService);
    }
}
