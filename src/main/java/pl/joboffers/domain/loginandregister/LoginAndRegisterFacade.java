package pl.joboffers.domain.loginandregister;

import lombok.AllArgsConstructor;
import pl.joboffers.domain.loginandregister.dto.RegisterUserDto;
import pl.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import pl.joboffers.domain.loginandregister.dto.UserDto;

@AllArgsConstructor
public class LoginAndRegisterFacade {
    private final UserRepository userRepository;

    public RegistrationResultDto register(RegisterUserDto registerUserDto) {
        if (registerUserDto.username() == null || registerUserDto.password() == null) {
            throw new InvalidUserCredentialsException("Both username and password cannot be null");
        }
        User user = UserMapper.mapFromUserDto(registerUserDto);
        User savedUser = userRepository.save(user);
        return RegistrationResultDto.builder()
                .userId(savedUser.userId())
                .wasCreated(true)
                .username(registerUserDto.username())
                .build();
    }

    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserDto(user.userId(), user.username(), user.password()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
