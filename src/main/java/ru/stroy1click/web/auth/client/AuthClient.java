package ru.stroy1click.web.auth.client;

import ru.stroy1click.web.auth.model.AuthRequest;
import ru.stroy1click.web.auth.model.JwtResponse;
import ru.stroy1click.web.auth.model.RefreshTokenRequest;
import ru.stroy1click.web.user.dto.UserDto;

public interface AuthClient {

    void registration(UserDto user);

    JwtResponse login(AuthRequest authRequest);

    void logout(RefreshTokenRequest refreshTokenRequest);
}
