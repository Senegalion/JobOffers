package pl.joboffers.infrastructure.offer.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.joboffers.domain.offer.OfferNotFoundException;

@ControllerAdvice
@Slf4j
public class OfferControllerErrorHandler {
    @ExceptionHandler(OfferNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public OfferErrorResponse getOfferNotFoundResponse(OfferNotFoundException offerNotFoundException) {
        String errorMessage = offerNotFoundException.getMessage();
        log.error(errorMessage);
        return OfferErrorResponse.builder()
                .message(errorMessage)
                .status(HttpStatus.NOT_FOUND)
                .build();
    }
}
