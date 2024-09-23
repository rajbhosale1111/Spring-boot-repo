package com.at.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Record not found Exception.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends RuntimeException {

  /**
   * Default Constructor.
   */
  public RecordNotFoundException() {
    super("Record not found");
  }

  /**
   * Constructor.
   *
   * @param message Exception message
   */
  public RecordNotFoundException(String message) {
    super(message);
  }
}