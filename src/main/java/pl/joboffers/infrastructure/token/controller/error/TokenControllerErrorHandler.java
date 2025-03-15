package pl.joboffers.infrastructure.token.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class TokenControllerErrorHandler {

    public static final String BAD_CREDENTIALS = "Bad credentials";

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public TokenErrorResponse handleBadCredentials(BadCredentialsException badCredentialsException) {
        return TokenErrorResponse.builder()
                .message(BAD_CREDENTIALS)
                .status(HttpStatus.UNAUTHORIZED)
                .build();
    }
}
