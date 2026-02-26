package ru.stroy1click.web.security;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ValidationException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.stroy1click.common.exception.NotFoundException;
import ru.stroy1click.common.service.JwtService;
import ru.stroy1click.web.auth.client.TokenClient;
import ru.stroy1click.web.auth.dto.RefreshTokenRequest;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenLifecycleInterceptor implements ClientHttpRequestInterceptor {

    private final TokenClient tokenClient;

    private final JwtService jwtService;

    @NotNull
    @Override
    public ClientHttpResponse intercept(@NotNull HttpRequest request,
                                        @NotNull byte[] body, @NotNull ClientHttpRequestExecution execution) throws IOException {
        List<String> authorizationHeader = request.getHeaders().get("Authorization");

        if(authorizationHeader != null){
            String token = authorizationHeader.getFirst().substring(7);

            boolean isJwtExpired = this.jwtService.isTokenExpired(token);

            if(isJwtExpired){
                RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(SecurityUtils.getRefreshToken());

                try {
                    String updatedToken = this.tokenClient.refreshAccessToken(refreshTokenRequest).getAccessToken();

                    request.getHeaders().setBearerAuth(updatedToken);
                    SecurityUtils.setAccessToken(updatedToken);
                } catch (NotFoundException | ValidationException e) {
                    SecurityContextHolder.clearContext();

                    throw new CredentialsExpiredException("Session expired");
                }
            }
        }

        return execution.execute(request, body);
    }
}