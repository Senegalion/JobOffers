package pl.joboffers.domain.register;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RegisterConfiguration {
    @Bean
    RegisterFacade loginAndRegisterFacade(UserRepository userRepository) {
        return new RegisterFacade(userRepository);
    }
}
