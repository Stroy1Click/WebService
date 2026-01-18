package ru.stroy1click.web.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import ru.stroy1click.web.common.exception.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ValidationErrorUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String collectErrorsToString(List<FieldError> fieldErrors){
        return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString()
                .replace("[", "").replace("]", "");
    }

    public static void validateStatus(ClientHttpResponse response) throws IOException {
        String errorBody = extractErrorDetail(response);
        HttpStatus httpStatus = HttpStatus.resolve(response.getStatusCode().value());

        if (httpStatus == null)  throw new RuntimeException("Неизвестный HTTP статус: " + response.getStatusCode().value());

        if (httpStatus.is4xxClientError()) {
            throwOnUserError(httpStatus, errorBody);
        } else {
            throwOnServerError(httpStatus, errorBody);
        }
    }

    public static void validateAuthenticationStatus(ClientHttpResponse response) {
        String errorBody = extractErrorDetail(response);

        throw new BadCredentialsException(errorBody);
    }

    private static String extractErrorDetail(ClientHttpResponse response) {
        try {
            byte[] bodyBytes = response.getBody().readAllBytes();
            if (bodyBytes.length == 0) throw new RuntimeException("длина bodyBytes равна 0");

            ProblemDetail problem = objectMapper.readValue(bodyBytes, ProblemDetail.class);

            return problem.getDetail() != null ? problem.getDetail() : problem.getTitle();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка обработки сообщения");
        }
    }

    private static void throwOnServerError(HttpStatus httpStatus, String errorMessage){
        switch (httpStatus){
            case SERVICE_UNAVAILABLE -> throw new ServiceUnavailableException(errorMessage);
            case INTERNAL_SERVER_ERROR -> throw new ServiceErrorResponseException(errorMessage);
            default -> throw new RuntimeException("Неизвестный http статус: " + httpStatus.value());

        }
    }

    private static void throwOnUserError(HttpStatus httpStatus, String errorMessage){
        switch (httpStatus){
            case NOT_FOUND -> throw new NotFoundException(errorMessage);
            case BAD_REQUEST -> throw new ValidationException(errorMessage);
            case CONFLICT -> throw new AlreadyExistsException(errorMessage);
            default -> throw new RuntimeException("Неизвестный http статус: " + httpStatus.value());
        }
    }

}
