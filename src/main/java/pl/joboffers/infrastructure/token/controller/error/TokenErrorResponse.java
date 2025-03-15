package pl.joboffers.infrastructure.token.controller.error;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record TokenErrorResponse(
        String message,
        HttpStatus status
) {
}
