package com.example.socialmediarestapi.config;

import com.example.socialmediarestapi.exception.JWTExpiredException;
import com.example.socialmediarestapi.repository.UserRepository;
import com.example.socialmediarestapi.security.authentication.jwt.JWTGrantedAuthoritiesConverter;
import com.example.socialmediarestapi.security.authentication.jwt.filter.ExceptionHandlerFilter;
import com.example.socialmediarestapi.security.authentication.jwt.filter.JWTCookieBearerTokenAuthenticationFilter;
import com.example.socialmediarestapi.service.JWTService;
import com.example.socialmediarestapi.service.UserDetailsServiceImplementation;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.time.Instant;
import java.util.*;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    public static List<RequestMatcher> allowedEndpoints = new ArrayList<>(Arrays.asList(
            new AntPathRequestMatcher("/login"),
            new AntPathRequestMatcher("/login/refresh_token"),
            new AntPathRequestMatcher("/api/user", "POST"),
            new AntPathRequestMatcher("/logout"),
            new AntPathRequestMatcher("/token")
    ));
    private final AuthenticationConfiguration config;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    public JwtDecoder decoder = new JwtDecoder() {
        @Override
        public Jwt decode(String token) throws JwtException {

            try {
                jwtService.checkJWTValidity(token);
            } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }


            JWT parsedJWT = null;
            try {
                parsedJWT = JWTParser.parse(token);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Jwt jwt = createJwt(token, parsedJWT);

            if (!new Date(Objects.requireNonNull(jwt.getExpiresAt()).getEpochSecond()).before(new Date()))
                return jwt;
            else
                throw new JWTExpiredException("Token is expired");

        }

        @Bean
        private Jwt createJwt(String token, JWT parsedJwt) {
            try {
                Map<String, Object> headers = new LinkedHashMap<>(parsedJwt.getHeader().toJSONObject());
                Map<String, Object> immutableClaims = parsedJwt.getJWTClaimsSet().getClaims();

                Map<String, Object> claims = new HashMap<>(immutableClaims);

                for (Map.Entry<String, Object> entry : claims.entrySet()) {
                    if (entry.getValue() instanceof Date) {
                        claims.replace(entry.getKey(), Instant.ofEpochSecond(((Date) entry.getValue()).getTime()));
                    }
                }


                return Jwt.withTokenValue(token)
                        .headers(h -> h.putAll(headers))
                        .claims(c -> c.putAll(claims))
                        .build();

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public SecurityConfig(AuthenticationConfiguration config, JWTService jwtService, UserRepository userRepository) {
        this.config = config;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(decoder);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JWTGrantedAuthoritiesConverter());

        jwtAuthenticationProvider.setJwtAuthenticationConverter(jwtAuthenticationConverter);
        auth.authenticationProvider(jwtAuthenticationProvider);

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        auth.getDefaultUserDetailsService();
        daoAuthenticationProvider.setUserDetailsService(new UserDetailsServiceImplementation(userRepository));

        auth.authenticationProvider(daoAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
                http
                        .csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(httpS -> httpS
                                .requestMatchers(allowedEndpoints.toArray(new RequestMatcher[0])).permitAll()
                                .anyRequest().authenticated())
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(bearerTokenAuthenticationFilter(), BasicAuthenticationFilter.class)
                        .addFilterBefore(new ExceptionHandlerFilter(), LogoutFilter.class)


                        .build();

    }


    @Bean
    JWTCookieBearerTokenAuthenticationFilter bearerTokenAuthenticationFilter() throws Exception {
        return new JWTCookieBearerTokenAuthenticationFilter(config.getAuthenticationManager(), securityContextRepository());
    }

    @Bean()
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

}
