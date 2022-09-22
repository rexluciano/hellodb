package com.rex.db.node.example;

import android.app.Application;
import android.content.Context;
import com.rex.hellodb.HelloDB;

public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    HelloDB.initialize(
        this,
        new HelloDB.Config()
            .serverUrl("https://api.hellodb.ga")
            .projectId("7XyTaA")
            .username("android")
            .password("android")
            .build());
  }

  @Override
  public void onTerminate() {
    super.onTerminate();
  }
}
