package com.rex.hellodb.exceptions;

import com.rex.hellodb.HelloDB;
import android.content.res.Resources.NotFoundException;

public class MissingResourcesException extends NotFoundException {
  private String message;

  @Override
  public String getMessage() {
    return new String(
        "One or more of the next values is missing from string resources: "
            + HelloDB.Configuration.SERVER_URL
            + ","
            + HelloDB.Configuration.PROJECT_ID
            + ","
            + HelloDB.Configuration.USERNAME
            + ", or"
            + HelloDB.Configuration.PASSWORD);
  }
}
