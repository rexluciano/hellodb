package com.rex.hellodb.events.listeners;

public interface QueryEventListener {
  void onDataChanged(String str);

  void onDataCancelled(Exception exception);
}
