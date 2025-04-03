package pl.joboffers.domain.register;

import pl.joboffers.domain.register.dto.RegisterUserDto;
import pl.joboffers.domain.register.dto.RegistrationResultDto;

class RegistrationBuilder {

    public static RegistrationResultDto build(RegisterUserDto registerUserDto, User savedUser) {
        return RegistrationResultDto.builder()
                .userId(savedUser.userId())
                .wasCreated(true)
                .username(registerUserDto.username())
                .build();
    }
}
