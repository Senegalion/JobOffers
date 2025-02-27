package pl.joboffers.domain.loginandregister;

public class LoginAndRegisterConfiguration {
    LoginAndRegisterFacade createForTest(UserRepository userRepository) {
        return new LoginAndRegisterFacade(userRepository);
    }
}
