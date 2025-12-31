package ru.stroy1click.web.security;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.stroy1click.web.auth.model.JwtResponse;

public class JwtExtractor {
    public static String getJwt(){
        JwtResponse jwtResponse = (JwtResponse) SecurityContextHolder.getContext()
                .getAuthentication().getCredentials();
        return jwtResponse.getAccessToken();
    }
}
