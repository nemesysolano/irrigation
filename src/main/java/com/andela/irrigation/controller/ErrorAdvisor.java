package com.andela.irrigation.controller;

import com.andela.irrigation.dto.ErrorResponse;
import com.andela.irrigation.service.NonExistingEntityError;
import com.andela.irrigation.ApplicationError;
import com.andela.irrigation.service.ValidationError;
import com.andela.irrigation.service.ConflictError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ErrorAdvisor {
    ErrorResponse toErrorResponse(ApplicationError cause) {
        return  ErrorResponse
            .builder()
            .message(cause.getMessage())
            .code(cause.serviceApplicationErrorCode.code)
            .errorMap(
                    cause.getErrorMap().entrySet().stream().collect(
                        Collectors.toMap(Map.Entry::getKey, e ->e.getValue().message
                    )
            ))
            .build();
    }
    @ExceptionHandler(value = {ValidationError.class})
    public ResponseEntity<ErrorResponse> onValidationError(ValidationError cause, WebRequest request) {
        ErrorResponse message = toErrorResponse(cause);

        log.error("Operation failed because a validation error.", cause);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NonExistingEntityError.class})
    public ResponseEntity<ErrorResponse> onNonExistingEntityError(NonExistingEntityError cause, WebRequest request) {
        ErrorResponse message = toErrorResponse(cause);

        log.error("Operation failed because a search operation yielded not results.", cause);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ConflictError.class})
    public ResponseEntity<ErrorResponse> onConflictError(ConflictError cause, WebRequest request) {
        ErrorResponse message = toErrorResponse(cause);

        log.error("Operation failed because the request contained duplicated info", cause);
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {AsynchronousError.class})
    public ResponseEntity<ErrorResponse> onAsynchronousError(AsynchronousError cause, WebRequest request) {
        ErrorResponse message = toErrorResponse(cause);

        log.error("Operation failed because an asynchronous operation failed.", cause);
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
