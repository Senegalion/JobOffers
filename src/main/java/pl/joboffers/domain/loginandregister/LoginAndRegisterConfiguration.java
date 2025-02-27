package pl.joboffers.domain.loginandregister;

class LoginAndRegisterConfiguration {
    LoginAndRegisterFacade createForTest(UserRepository userRepository) {
        return new LoginAndRegisterFacade(userRepository);
    }
}
