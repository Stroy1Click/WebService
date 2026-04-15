package ru.stroy1click.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.stroy1click.common.exception.*;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class RestAdviceController {

    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail problemDetail(NotFoundException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle(
                this.messageSource.getMessage(
                        "error.title.not_found",
                        null,
                        Locale.getDefault()
                )
        );
        return problemDetail;
    }

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail problemDetail(ValidationException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle(
                this.messageSource.getMessage(
                        "error.title.validation",
                        null,
                        Locale.getDefault()
                )
        );
        return problemDetail;
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ProblemDetail problemDetail(AlreadyExistsException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle(
                this.messageSource.getMessage(
                        "error.title.already_exist",
                        null,
                        Locale.getDefault()
                )
        );
        return problemDetail;
    }
}
