package com.jacamachof.devsuapirest.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to catch and parse errors coming from the validation library
 */
@ControllerAdvice
public class ValidationExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        var response = new ErrorResponse(HttpStatus.BAD_REQUEST, request);

        e.getBindingResult().getFieldErrors().forEach(error ->
                response.getErrors().add(new ErrorObject(error)));

        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, request, e.getMessage());
    }

    @Getter
    static class ErrorResponse {
        private final Long timestamp;
        private final Integer status;
        private final String error;
        private final String path;
        private final String message;
        private final List<ErrorObject> errors = new ArrayList<>();

        ErrorResponse(HttpStatus status, HttpServletRequest request) {
            this.status = status.value();
            this.error = status.getReasonPhrase();
            this.path = request.getServletPath();
            this.message = null;
            this.timestamp = System.currentTimeMillis();
        }

        ErrorResponse(HttpStatus status, HttpServletRequest request, String message) {
            this.status = status.value();
            this.error = status.getReasonPhrase();
            this.path = request.getServletPath();
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }
    }

    @Getter
    static class ErrorObject {
        private final String field;
        private final String code;
        private final String message;

        ErrorObject(FieldError error) {
            field = error.getField();
            code = error.getCode();
            message = error.getDefaultMessage();
        }
    }
}
