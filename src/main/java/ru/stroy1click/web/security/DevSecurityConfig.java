package ru.stroy1click.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class DevSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.GET, "/api/v1/orders/user")
                                .authenticated()
                                .requestMatchers(HttpMethod.POST,"/api/v1/product-attribute-assignments/filter", "/api/v1/confirmation-codes/**",
                                        "/api/v1/confirmation-codes", "/api/v1/auth/registration")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/admin/**")
                                .hasRole("ADMIN")
                                .requestMatchers( "/", "/css/**", "/images/**", "/js/**", "/account/**",
                                        "/categories", "/categories/**","/subcategories/**", "/subcategories",
                                        "/product-types/**", "/product-types", "/products/**", "/error", "/error/**", "/contacts", "/about-us")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .invalidSessionUrl("/invalid-session")
                        .maximumSessions(6)
                        .maxSessionsPreventsLogin(true))
                .formLogin(login -> login
                        .loginPage("/account/login")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/account/login?error"))
                .logout(logout ->
                        logout.logoutUrl("/logout")
                                .logoutSuccessUrl("/account/login"));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return this.customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
