package com.rex.hellodb.exceptions;

import android.content.pm.PackageManager.NameNotFoundException;

public class MissingComponentException extends NameNotFoundException {

  private String message;

  public MissingComponentException(String message) {
    super(message);
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message == null ? "" : message;
  }
}
