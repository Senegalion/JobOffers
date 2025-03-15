package pl.joboffers.infrastructure.loginandregister.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.domain.loginandregister.LoginAndRegisterFacade;
import pl.joboffers.domain.loginandregister.dto.RegisterUserDto;
import pl.joboffers.domain.loginandregister.dto.RegistrationResultDto;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class LoginAndRegisterController {
    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResultDto> registerUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        String encodedPassword = passwordEncoder.encode(registerUserDto.password());
        RegistrationResultDto registrationResultDto =
                loginAndRegisterFacade.register(
                        new RegisterUserDto(registerUserDto.username(), encodedPassword)
                );
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResultDto);
    }
}
