package pl.joboffers.domain.offer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface OfferRepository extends MongoRepository<Offer, String> {

    boolean existsByOfferUrl(String offerUrl);
}
