package pl.joboffers.infrastructure.token.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.infrastructure.security.jwt.JwtAuthenticatorFacade;
import pl.joboffers.infrastructure.token.controller.dto.JwtResponseDto;
import pl.joboffers.infrastructure.token.controller.dto.TokenRequestDto;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class TokenController {
    private final JwtAuthenticatorFacade jwtAuthenticatorFacade;

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> authenticateAndGenerateToken(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
        final JwtResponseDto jwtResponseDto = jwtAuthenticatorFacade.authenticateAndGenerateToken(tokenRequestDto);
        return ResponseEntity.ok(jwtResponseDto);
    }
}
