
package ru.stroy1click.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.stroy1click.common.exception.*;


import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class AdviceController {

    private final MessageSource messageSource;

    @ExceptionHandler(ServiceUnavailableException.class)
    public String handleException(ServiceUnavailableException exception, Model model){
        model.addAttribute("error",   this.messageSource.getMessage(
                "error.description.service_unavailable",
                null,
                Locale.getDefault()
        ));

        return "error/500";
    }

    @ExceptionHandler(ServiceErrorResponseException.class)
    public String handleException(ServiceErrorResponseException exception, Model model){
        model.addAttribute("error",   this.messageSource.getMessage(
                "error.description.service_unavailable",
                null,
                Locale.getDefault()
        ));

        return "error/500";
    }
}

