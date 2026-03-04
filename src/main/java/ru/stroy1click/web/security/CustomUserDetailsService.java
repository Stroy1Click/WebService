package ru.stroy1click.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.stroy1click.common.service.JwtService;
import ru.stroy1click.web.auth.dto.JwtResponse;
import ru.stroy1click.web.user.client.UserClient;
import ru.stroy1click.web.user.dto.Role;
import ru.stroy1click.web.user.dto.UserDto;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserClient userClient;

    private final JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = this.userClient.getByEmail(username, this.jwtService.generate("admin@gmail.com", Role.ROLE_ADMIN.toString(), true));

        return new CustomUserDetails(userDto, new JwtResponse());
    }
}
