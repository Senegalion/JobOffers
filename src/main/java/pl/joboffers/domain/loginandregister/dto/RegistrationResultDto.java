package pl.joboffers.domain.loginandregister.dto;

import lombok.Builder;

@Builder
public record RegistrationResultDto(String userId, boolean wasCreated, String username) {
}
