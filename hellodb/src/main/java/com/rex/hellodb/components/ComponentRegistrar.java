package com.rex.hellodb.components;

import android.os.Binder;
import com.rex.hellodb.components.ComponentDiscoveryService;

public class ComponentRegistrar extends Binder {
  public ComponentDiscoveryService getService() {
    return new ComponentDiscoveryService();
  }
}
