package pl.joboffers.domain.loginandregister;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.joboffers.domain.loginandregister.dto.RegisterUserDto;
import pl.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import pl.joboffers.domain.loginandregister.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginAndRegisterFacadeTest {
    private final UserRepository userRepository = new UserRepositoryTestImpl();

    @Test
    public void shouldRegisterUser() {
        LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterConfiguration()
                .createForTest(userRepository);
        String testUsername = TestCredentials.getUsername();
        String testPassword = TestCredentials.getPassword();
        RegisterUserDto registerUserDto = new RegisterUserDto(testUsername, testPassword);

        RegistrationResultDto register = loginAndRegisterFacade.register(registerUserDto);

        assertAll(
                () -> assertThat(register.wasCreated()).isTrue(),
                () -> assertThat(register.username()).isEqualTo(testUsername)
        );
    }

    @Test
    public void shouldThrowExceptionWhenUserEntersUsernameThatWasNull() {
        LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterConfiguration()
                .createForTest(userRepository);
        String testUsername = null;
        String testPassword = TestCredentials.getPassword();
        RegisterUserDto registerUserDto = new RegisterUserDto(testUsername, testPassword);

        assertThrows(InvalidUserCredentialsException.class, () -> loginAndRegisterFacade.register(registerUserDto));
    }

    @Test
    public void shouldThrowExceptionWhenUserEntersPasswordThatWasNull() {
        LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterConfiguration()
                .createForTest(userRepository);
        String testUsername = TestCredentials.getUsername();
        String testPassword = null;
        RegisterUserDto registerUserDto = new RegisterUserDto(testUsername, testPassword);

        assertThrows(InvalidUserCredentialsException.class, () -> loginAndRegisterFacade.register(registerUserDto));
    }

    @Test
    public void shouldFindUserByUsername() {
        LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterConfiguration()
                .createForTest(userRepository);
        String testUsername = TestCredentials.getUsername();
        String testPassword = TestCredentials.getPassword();
        RegisterUserDto registerUserDto = new RegisterUserDto(testUsername, testPassword);
        RegistrationResultDto registrationResultDto = loginAndRegisterFacade.register(registerUserDto);

        UserDto foundUser = loginAndRegisterFacade.findByUsername(testUsername);

        assertThat(foundUser).isEqualTo(new UserDto(registrationResultDto.userId(), testUsername, testPassword));
    }

    @Test
    public void shouldThrowExceptionWhenUserHasNotBeenFound() {
        LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterConfiguration()
                .createForTest(userRepository);
        String testUsername = TestCredentials.getUsername();

        assertThrows(BadCredentialsException.class, () -> loginAndRegisterFacade.findByUsername(testUsername));
    }
}