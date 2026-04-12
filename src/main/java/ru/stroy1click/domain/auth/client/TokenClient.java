package ru.stroy1click.domain.auth.client;

import ru.stroy1click.domain.auth.dto.JwtResponse;
import ru.stroy1click.domain.auth.dto.RefreshTokenRequest;

public interface TokenClient {

    JwtResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest);
}
