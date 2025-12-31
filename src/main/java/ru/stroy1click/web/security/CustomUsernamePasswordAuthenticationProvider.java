package ru.stroy1click.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.stroy1click.web.auth.client.AuthClient;
import ru.stroy1click.web.auth.model.AuthRequest;
import ru.stroy1click.web.auth.model.JwtResponse;
import ru.stroy1click.web.common.exception.ValidationException;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;

    private final AuthClient authClient;

    private final PasswordEncoder passwordEncoder;

    private final MessageSource messageSource;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
        try {
            JwtResponse jwtResponse = this.authClient.login(new AuthRequest(username, password));

            return new UsernamePasswordAuthenticationToken(
                    userDetails,
                    jwtResponse,
                    userDetails.getAuthorities()
            );
        } catch (ValidationException e){
            throw new ValidationException(
                    this.messageSource.getMessage(
                            "error.title.invalid_credentials",
                            null,
                            Locale.getDefault()
                    )
            );
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
