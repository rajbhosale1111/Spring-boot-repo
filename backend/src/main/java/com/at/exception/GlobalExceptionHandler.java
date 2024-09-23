package com.at.exception;

import com.at.dao.response.RestApiError;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({RecordNotFoundException.class})
  @ResponseBody
  public RestApiError handleCustomException(Exception ex, WebRequest request) {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    Optional<Annotation> annotationOptional = Arrays.stream(ex.getClass().getAnnotations())
        .filter(ResponseStatus.class::isInstance).findFirst();
    if (annotationOptional.isPresent()) {
      ResponseStatus annotation = (ResponseStatus) annotationOptional.get();
      status = annotation.value();
    }
    return RestApiError.builder().status(status.value()).message(ex.getMessage()).build();
  }

  @ExceptionHandler({AuthenticationException.class})
  @ResponseBody
  public ResponseEntity<RestApiError> handleAuthenticationException(Exception ex) {
    RestApiError re = RestApiError.builder().status(HttpStatus.UNAUTHORIZED.value())
        .message("Authentication failed - " + ex.getMessage()).build();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(re);
  }
}
