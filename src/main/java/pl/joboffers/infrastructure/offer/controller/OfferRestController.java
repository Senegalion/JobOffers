package pl.joboffers.infrastructure.offer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/offers")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class OfferRestController {
    OfferFacade offerFacade;

    @GetMapping
    public ResponseEntity<List<OfferResponseDto>> getAllOffers() {
        List<OfferResponseDto> allOffers = offerFacade.findAllOffers();
        return ResponseEntity.ok(allOffers);
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<OfferResponseDto> getOfferById(@PathVariable String offerId) {
        OfferResponseDto offerFound = offerFacade.findOfferById(offerId);
        return ResponseEntity.ok(offerFound);
    }

    @PostMapping()
    public ResponseEntity<OfferResponseDto> addOffer(@RequestBody @Valid OfferRequestDto offer) {
        OfferResponseDto offerResponseDto = offerFacade.saveOffer(offer);
        return ResponseEntity.status(HttpStatus.CREATED).body(offerResponseDto);
    }
}
