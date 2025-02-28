package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;

import java.util.List;

@AllArgsConstructor
public class OfferFacade {
    private final OfferRepository offerRepository;

    public List<OfferResponseDto> findAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return offers.stream()
                .map(OfferMapper::mapToOfferResponseDto)
                .toList();
    }

    public OfferResponseDto findOfferById(String id) {
        Offer offer = offerRepository.findById(id);
        return OfferMapper.mapToOfferResponseDto(offer);
    }

    public OfferResponseDto saveOffer(OfferRequestDto offerRequestDto) {
        Offer offer = OfferMapper.mapFromOfferRequestDto(offerRequestDto);
        Offer savedOffer = offerRepository.save(offer);
        return OfferMapper.mapToOfferResponseDto(savedOffer);
    }
}
