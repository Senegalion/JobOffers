package pl.joboffers.infrastructure.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "auth.jwt")
public record JwtConfigurationProperties(
        String secretKey,
        long expirationDays,
        String issuer
) {
}
