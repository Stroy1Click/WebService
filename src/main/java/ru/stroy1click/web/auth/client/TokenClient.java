package ru.stroy1click.web.auth.client;

import ru.stroy1click.web.auth.dto.JwtResponse;
import ru.stroy1click.web.auth.dto.RefreshTokenRequest;

public interface TokenClient {

    JwtResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest);
}
