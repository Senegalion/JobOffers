package pl.joboffers.domain.offer;

import java.util.List;

interface OfferRepository {
    List<Offer> findAll();

    Offer findById(String id);

    Offer save(Offer offer);
}
