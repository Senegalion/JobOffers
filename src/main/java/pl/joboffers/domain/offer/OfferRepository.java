package pl.joboffers.domain.offer;

import java.util.List;
import java.util.Optional;

interface OfferRepository {
    List<Offer> findAll();

    Optional<Offer> findById(String id);

    Offer save(Offer offer);
}
