package com.rex.hellodb;

import android.content.Context;

public class ResourcesHelper {
  public static String getStringResourceByKey(Context context, String resourceKey) {
    int resId =
        context.getResources().getIdentifier(resourceKey, "string", context.getPackageName());
    return context.getResources().getString(resId);
  }
}
