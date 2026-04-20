package ru.stroy1click.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.stroy1click.infrastructure.captcha.service.CaptchaService;

import java.io.IOException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CaptchaFilter extends OncePerRequestFilter {

    private final CaptchaService captchaService;

    private final MessageSource messageSource;

    private final SimpleUrlAuthenticationFailureHandler failureHandler =
            new SimpleUrlAuthenticationFailureHandler("/account/login?error");

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        if("POST".equals(request.getMethod()) && "/process_login".equals(request.getServletPath())) {
            String smartToken = request.getParameter("smart-token");

            if(smartToken == null || !this.captchaService.validate(smartToken)) {
                String errorMessage = this.messageSource.getMessage(
                        "validation.captcha.invalid",
                        null,
                        Locale.getDefault()
                );
                failureHandler.onAuthenticationFailure(request,response,new BadCredentialsException(errorMessage));
                return;
            }
        }
        filterChain.doFilter(request,response);
    }
}
