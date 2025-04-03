package pl.joboffers.domain.register;

import pl.joboffers.domain.register.dto.RegisterUserDto;

class UserMapper {
    public static User mapFromRegisterUserDto(RegisterUserDto registerUserDto) {
        return User.builder()
                .username(registerUserDto.username())
                .password(registerUserDto.password())
                .build();
    }
}
