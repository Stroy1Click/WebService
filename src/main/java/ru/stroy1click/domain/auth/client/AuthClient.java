package ru.stroy1click.domain.auth.client;

import ru.stroy1click.domain.auth.dto.AuthRequest;
import ru.stroy1click.domain.auth.dto.JwtResponse;
import ru.stroy1click.domain.auth.dto.RefreshTokenRequest;
import ru.stroy1click.domain.user.dto.UserDto;

public interface AuthClient {

    void registration(UserDto user);

    JwtResponse login(AuthRequest authRequest);

    void logout(RefreshTokenRequest refreshTokenRequest);
}
