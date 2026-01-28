package ru.stroy1click.web.user.client;

import ru.stroy1click.web.user.dto.UserDto;

public interface UserClient {

    UserDto get(Long id, String jwt);

    void update(Long id, UserDto userDto, String jwt);

    void delete(Long id, String jwt);

    UserDto getByEmail(String email, String jwt);

}
