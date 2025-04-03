package pl.joboffers.domain.register.dto;

import lombok.Builder;

@Builder
public record RegistrationResultDto(
        String userId,
        boolean wasCreated,
        String username
) {
}
