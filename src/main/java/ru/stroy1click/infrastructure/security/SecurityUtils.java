package ru.stroy1click.infrastructure.security;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.stroy1click.domain.user.dto.Role;

public class SecurityUtils {

    public static String getAccessToken(){
        CustomUserDetails customUserDetails = getCustomUserDetails();

        return customUserDetails.getJwtResponse().getAccessToken();
    }

    public static void setAccessToken(String accessToken){
        CustomUserDetails customUserDetails = getCustomUserDetails();

        customUserDetails.getJwtResponse().setAccessToken(accessToken);
    }

    public static String getRefreshToken(){
        CustomUserDetails customUserDetails = getCustomUserDetails();

        return customUserDetails.getJwtResponse().getRefreshToken();
    }

    public static Long getUserId(){
        CustomUserDetails userDetail = getCustomUserDetails();

        return userDetail.getUser().getId();
    }

    public static String getEmail(){
        CustomUserDetails userDetail = getCustomUserDetails();

        return userDetail.getUser().getEmail();
    }

    public static Role getRole(){
        CustomUserDetails userDetail = getCustomUserDetails();

        return userDetail.getUser().getRole();
    }

    private static CustomUserDetails getCustomUserDetails(){
        return (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }
}
