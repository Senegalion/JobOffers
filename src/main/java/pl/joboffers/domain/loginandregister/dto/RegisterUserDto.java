package pl.joboffers.domain.loginandregister.dto;

import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
public record RegisterUserDto(
        @NotNull(message = "{username.not.null}")
        @NotEmpty(message = "{username.not.empty}")
        String username,
        @NotNull(message = "{password.not.null}")
        @NotEmpty(message = "{password.not.empty}")
        String password
) {
}
