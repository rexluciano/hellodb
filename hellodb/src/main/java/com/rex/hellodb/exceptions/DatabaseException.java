package com.rex.hellodb.exceptions;

import com.rex.hellodb.exceptions.DatabaseException;

public class DatabaseException extends Exception {

  private String message;

  public DatabaseException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message == null ? "" : message;
  }
}
