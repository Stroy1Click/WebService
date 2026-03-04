package ru.stroy1click.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsChecker implements UserDetailsChecker {

    private final MessageSource messageSource;

    @Override
    public void check(UserDetails toCheck) {
        if(!toCheck.isEnabled()){
            throw new DisabledException(
                    this.messageSource.getMessage(
                            "error.authentication.email_not_confirmed",
                            null,
                            Locale.getDefault()
                    )
            );
        }
    }
}
