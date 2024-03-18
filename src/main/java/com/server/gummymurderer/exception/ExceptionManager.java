package com.server.gummymurderer.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> AppExceptionHandler(AppException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(new ErrorResponse(e.getErrorCode().name(), e.toString())));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> sqlExceptionHandler(SQLException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(new ErrorResponse(ErrorCode.DATABASE_ERROR.name(), e.toString())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorResponse> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return new ErrorResponse(validationErrorCode(fieldName), errorMessage);
                })
                .collect(Collectors.toList());

        log.info("🐻ValidationError 발생");
        log.info("🐻Request URI : {}", request.getRequestURI());
        log.info("🐻Request Method : {}", request.getMethod());
        log.info("🐻Validation Errors : {}", errors);

        return ResponseEntity.badRequest().body(Response.error(errors));
    }

    private String validationErrorCode(String fieldName) {
        Map<String, String> errorCodes = Map.of(
                "password", ErrorCode.INVALID_PASSWORD.name(),
                "email", ErrorCode.INVALID_EMAIL.name(),
                "nickname", ErrorCode.INVALID_NICKNAME.name(),
                "name", ErrorCode.INVALID_NAME.name()
        );
        return errorCodes.getOrDefault(fieldName, ErrorCode.VALIDATION_ERROR.name());
    }


}
