package pl.joboffers.domain.loginandregister;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryTestImpl implements UserRepository {
    Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        UUID userid = UUID.randomUUID();
        User userToBeSaved = User.builder()
                .userId(userid.toString())
                .username(user.username())
                .password(user.password())
                .build();
        users.put(userToBeSaved.username(), userToBeSaved);
        return userToBeSaved;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
}
