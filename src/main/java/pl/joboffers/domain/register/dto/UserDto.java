package pl.joboffers.domain.register.dto;

public record UserDto(
        String userId,
        String username,
        String password
) {
}
