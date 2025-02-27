package pl.joboffers.domain.loginandregister.dto;

import lombok.Builder;

@Builder
public record RegisterUserDto(String username, String password) {
}
