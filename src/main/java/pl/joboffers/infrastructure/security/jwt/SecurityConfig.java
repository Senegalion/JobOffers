package pl.joboffers.infrastructure.security.jwt;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.joboffers.domain.register.RegisterFacade;

import java.util.Arrays;
import java.util.List;

@Configuration
@AllArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {
    private final JwtAuthTokenFiler jwtAuthTokenFiler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(RegisterFacade registerFacade) {
        return new LoginUserDetailsService(registerFacade);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new UrlBasedCorsConfigurationSource();
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("http://localhost", "http://localhost:3000", "http://ec2-18-199-167-57.eu-central-1.compute.amazonaws.com:8000"));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    corsConfig.registerCorsConfiguration("/**", config);
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/").permitAll()
                        .antMatchers("/swagger-ui/**").permitAll()
                        .antMatchers("/swagger-ui.html/**").permitAll()
                        .antMatchers("/v3/api-docs/**").permitAll()
                        .antMatchers("/webjars/**").permitAll()
                        .antMatchers("/token/**").permitAll()
                        .antMatchers("/register/**").permitAll()
                        .antMatchers("/swagger-resources/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtAuthTokenFiler, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
