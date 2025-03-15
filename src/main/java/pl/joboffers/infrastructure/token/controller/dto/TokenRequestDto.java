package pl.joboffers.infrastructure.token.controller.dto;

import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
public record TokenRequestDto(
        @NotEmpty(message = "{username.not.empty}")
        @NotNull(message = "{username.not.null}")
        String username,
        @NotEmpty(message = "{password.not.empty}")
        @NotNull(message = "{password.not.null}")
        String password
) {
}
