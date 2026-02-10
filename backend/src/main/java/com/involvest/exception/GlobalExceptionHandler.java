package com.involvest.exception;

import java.time.OffsetDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ApiError> handleApi(ApiException ex) {
    HttpStatus status = switch (ex.getCode()) {
      case "NOT_FOUND" -> HttpStatus.NOT_FOUND;
      case "UNAUTHORIZED" -> HttpStatus.UNAUTHORIZED;
      default -> HttpStatus.BAD_REQUEST;
    };
    return ResponseEntity.status(status)
        .body(new ApiError(ex.getCode(), ex.getMessage(), OffsetDateTime.now()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
    FieldError error = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
    String message = error == null ? "Validation error" : error.getField() + " " + error.getDefaultMessage();
    return ResponseEntity.badRequest()
        .body(new ApiError("VALIDATION_ERROR", message, OffsetDateTime.now()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneric(Exception ex) {
    logger.error("Unexpected error", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiError("INTERNAL_ERROR", "Unexpected error", OffsetDateTime.now()));
  }
}
