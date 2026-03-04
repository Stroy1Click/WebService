package ru.stroy1click.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.stroy1click.web.auth.client.AuthClient;
import ru.stroy1click.web.auth.dto.AuthRequest;
import ru.stroy1click.web.auth.dto.JwtResponse;

@Component
@RequiredArgsConstructor
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;

    private final AuthClient authClient;

    private final CustomUserDetailsChecker customUserDetailsChecker;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        CustomUserDetails userDetails = (CustomUserDetails) this.customUserDetailsService.loadUserByUsername(username);

        this.customUserDetailsChecker.check(userDetails);

        JwtResponse jwtResponse = this.authClient.login(new AuthRequest(username, password));
        userDetails.setJwtResponse(jwtResponse);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                jwtResponse.getAccessToken(),
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
