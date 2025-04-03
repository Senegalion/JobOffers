package pl.joboffers.domain.register;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import pl.joboffers.domain.register.dto.RegisterUserDto;
import pl.joboffers.domain.register.dto.RegistrationResultDto;
import pl.joboffers.domain.register.dto.UserDto;

@AllArgsConstructor
public class RegisterFacade {
    private final UserRepository userRepository;

    public RegistrationResultDto register(RegisterUserDto registerUserDto) {
        if (registerUserDto.username() == null || registerUserDto.password() == null) {
            throw new InvalidUserCredentialsException(
                    ExceptionMessage.USERNAME_AND_PASSWORD_NULL.getExceptionMessage()
            );
        }
        User user = UserMapper.mapFromRegisterUserDto(registerUserDto);
        User savedUser = userRepository.save(user);
        return RegistrationBuilder.build(registerUserDto, savedUser);
    }

    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserDto(user.userId(), user.username(), user.password()))
                .orElseThrow(
                        () -> new BadCredentialsException(
                                ExceptionMessage.USER_NOT_FOUND.getExceptionMessage()
                        )
                );
    }
}
