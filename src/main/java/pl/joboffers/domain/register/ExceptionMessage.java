package pl.joboffers.domain.register;

import lombok.Getter;

@Getter
enum ExceptionMessage {
    USER_NOT_FOUND("User not found"),
    USERNAME_AND_PASSWORD_NULL("Both username and password cannot be null");

    private final String exceptionMessage;

    ExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
