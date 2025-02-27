package pl.joboffers.domain.loginandregister;

import pl.joboffers.domain.loginandregister.dto.RegisterUserDto;

class UserMapper {
    public static User mapFromUserDto(RegisterUserDto registerUserDto) {
        return User.builder()
                .username(registerUserDto.username())
                .password(registerUserDto.password())
                .build();
    }
}
