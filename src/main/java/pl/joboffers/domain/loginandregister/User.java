package pl.joboffers.domain.loginandregister;

import lombok.Builder;

@Builder
public record User(String userId, String username, String password) {
}
