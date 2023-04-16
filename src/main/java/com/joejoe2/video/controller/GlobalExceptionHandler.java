package com.joejoe2.video.controller;

import com.joejoe2.video.data.ErrorMessageResponse;
import com.joejoe2.video.data.InvalidRequestResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return super.handleHttpMessageNotReadable(ex, headers, status, request);
  }

  @Override
  @ApiResponse(
      responseCode = "400",
      description = "field errors in request body/param",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = InvalidRequestResponse.class),
              examples =
                  @ExampleObject(
                      value =
                          "{\"errors\":{\"field1\":[\"msg1\",\"msg2\"], "
                              + "\"field2\":[...], ...}}")))
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    Map<String, TreeSet<String>> errors = new HashMap<>();
    for (FieldError error : ex.getFieldErrors()) {
      TreeSet<String> messages = errors.getOrDefault(error.getField(), new TreeSet<>());
      messages.add(error.getDefaultMessage());
      errors.put(error.getField(), messages);
    }
    return ResponseEntity.badRequest().body(new InvalidRequestResponse(errors));
  }

  @Override
  @ApiResponse(
      responseCode = "400",
      description = "field errors in request body/param",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = InvalidRequestResponse.class),
              examples =
                  @ExampleObject(
                      value =
                          "{\"errors\":{\"field1\":[\"msg1\",\"msg2\"], "
                              + "\"field2\":[...], ...}}")))
  protected ResponseEntity<Object> handleBindException(
      BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    Map<String, TreeSet<String>> errors = new HashMap<>();
    for (FieldError error : ex.getFieldErrors()) {
      TreeSet<String> messages = errors.getOrDefault(error.getField(), new TreeSet<>());
      messages.add(error.getDefaultMessage());
      errors.put(error.getField(), messages);
    }
    return ResponseEntity.badRequest().body(new InvalidRequestResponse(errors));
  }

  @ExceptionHandler(RuntimeException.class)
  @ApiResponse(
      responseCode = "500",
      description = "internal server error",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessageResponse.class)))
  public ResponseEntity handleRuntimeException(Exception ex, WebRequest request) {
    ex.printStackTrace();
    return new ResponseEntity<>(
        new ErrorMessageResponse("unknown error, please try again later !"),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
