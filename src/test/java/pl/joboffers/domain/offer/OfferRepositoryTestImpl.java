package pl.joboffers.domain.offer;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class OfferRepositoryTestImpl implements OfferRepository {
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
        if (offers.values().stream().anyMatch(offerToBeSaved -> offerToBeSaved.offerUrl().equals(offer.offerUrl()))) {
            throw new DuplicateKeyException(String.format("Offer with url: [%s] already exists", offer.offerUrl()));
        }

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
    public boolean existsByOfferUrl(String offerUrl) {
        List<Offer> retrievedOfferUrls = offers.values().stream()
                .filter(offer -> offerUrl.equals(offer.offerUrl()))
                .toList();
        return !retrievedOfferUrls.isEmpty();
    }

    @Override
    public <S extends Offer> List<S> saveAll(Iterable<S> entities) {
        List<Offer> newOffers = new ArrayList<>();
        entities.forEach(newOffers::add);

        return (List<S>) newOffers.stream()
                .map(this::save)
                .toList();
    }

    @Override
    public List<Offer> findAll(Sort sort) {
        return null;
    }

    @Override
    public <S extends Offer> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public Page<Offer> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<Offer> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Offer entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Offer> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Offer> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Offer> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Offer> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Offer> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Offer, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
