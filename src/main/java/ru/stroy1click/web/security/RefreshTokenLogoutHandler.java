package ru.stroy1click.web.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import ru.stroy1click.web.auth.client.AuthClient;
import ru.stroy1click.web.auth.dto.RefreshTokenRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenLogoutHandler implements LogoutHandler {

    private final AuthClient authClient;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String refreshToken = SecurityUtils.getRefreshToken();

        try {
            this.authClient.logout(new RefreshTokenRequest(refreshToken));
        } catch (Exception e) {
            log.error("logout error ", e);
        }
    }
}
