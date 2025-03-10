package pl.joboffers.infrastructure.offer.controller.error;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
public record OfferPostErrorResponse(
        List<String> errorMessages,
        HttpStatus status
) {
}
