package pl.joboffers.domain.loginandregister;

import lombok.Builder;

@Builder
record User(String userId, String username, String password) {
}
