package ru.stroy1click.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        CustomUserDetails userDetails = (CustomUserDetails) this.customUserDetailsService.loadUserByUsername(username);

        JwtResponse jwtResponse = this.authClient.login(new AuthRequest(username, password));
        userDetails.setJwtResponse(jwtResponse);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                jwtResponse.getAccessToken(),
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(token);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
