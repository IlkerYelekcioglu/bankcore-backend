package com.bankcore.bankcore_backend.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ProblemDetail handleNotFound(
      ResourceNotFoundException ex) {

    return createProblem(
        HttpStatus.NOT_FOUND,
        "Resource not found",
        ex.getMessage()
    );
  }

  @ExceptionHandler(DuplicateCustomerException.class)
  public ProblemDetail handleDuplicate(
      DuplicateCustomerException ex) {

    return createProblem(
        HttpStatus.CONFLICT,
        "Duplicate customer data",
        ex.getMessage()
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidation(
      MethodArgumentNotValidException ex) {

    Map<String, String> errors =
        ex.getBindingResult().getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                error -> error.getField(),
                error -> error.getDefaultMessage() == null
                    ? "Invalid value"
                    : error.getDefaultMessage(),
                (first, second) -> first
            ));

    ProblemDetail detail = createProblem(
        HttpStatus.BAD_REQUEST,
        "Validation failed",
        "Please check the request fields."
    );

    detail.setProperty("errors", errors);
    return detail;
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ProblemDetail handleDataConflict(
      DataIntegrityViolationException ex) {

    return createProblem(
        HttpStatus.CONFLICT,
        "Data conflict",
        "The request conflicts with a database constraint."
    );
  }

  private ProblemDetail createProblem(
      HttpStatus status,
      String title,
      String message) {

    ProblemDetail detail =
        ProblemDetail.forStatusAndDetail(status, message);

    detail.setTitle(title);
    detail.setType(
        URI.create("https://bankcore.example/errors/"
            + status.value())
    );

    return detail;
  }
}