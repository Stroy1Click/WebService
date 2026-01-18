package ru.stroy1click.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.stroy1click.web.auth.model.JwtResponse;
import ru.stroy1click.web.user.client.UserClient;
import ru.stroy1click.web.user.dto.UserDto;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = this.userClient.getByEmail(username);

        return new CustomUserDetails(userDto, new JwtResponse());
    }
}
