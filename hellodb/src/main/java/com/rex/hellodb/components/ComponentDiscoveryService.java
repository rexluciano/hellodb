package com.rex.hellodb.components;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.rex.hellodb.components.ComponentRegistrar;
import com.rex.hellodb.exceptions.MissingComponentException;

public class ComponentDiscoveryService extends Service {

  private static final String TAG = ComponentDiscoveryService.class.getSimpleName();
  private static final String SDK_VERSION = "1.0.0";
  private Bundle data;
  private ComponentRegistrar cr = new ComponentRegistrar();

  @Override
  public IBinder onBind(Intent intent) {
    return cr == null ? null : cr;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    ComponentName cn = new ComponentName(this, this.getClass());
    try {
      data =
          getApplicationContext()
              .getPackageManager()
              .getServiceInfo(cn, PackageManager.GET_META_DATA)
              .metaData;
      if (data.containsKey(
          "com.rex.hellodb.components:com.rex.hellodb.components.ComponentRegistrar")) {
        startComponent();
      } else {
        throw new MissingComponentException(
            "Cannot start this component without Component Registrar is added on your AndroidManifest.xml file.");
      }
    } catch (NameNotFoundException e) {
      throw new NullPointerException("Cannot start this component on null data: " + e.getMessage());
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  public String getComponentName() {
    return data == null
        ? ""
        : data.getString(
            "com.rex.hellodb.components:com.rex.hellodb.components.ComponentRegistrar");
  }

  private void startComponent() {
    Log.i(TAG, "Service is running...");
  }

  public String getSDKVersion() {
    return SDK_VERSION;
  }
}
