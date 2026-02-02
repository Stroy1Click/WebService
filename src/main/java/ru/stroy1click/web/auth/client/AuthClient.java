package ru.stroy1click.web.auth.client;

import ru.stroy1click.web.auth.dto.AuthRequest;
import ru.stroy1click.web.auth.dto.JwtResponse;
import ru.stroy1click.web.auth.dto.RefreshTokenRequest;
import ru.stroy1click.web.user.dto.UserDto;

public interface AuthClient {

    void registration(UserDto user);

    JwtResponse login(AuthRequest authRequest);

    void logout(RefreshTokenRequest refreshTokenRequest);
}
