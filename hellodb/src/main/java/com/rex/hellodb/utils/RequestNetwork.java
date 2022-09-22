package com.rex.hellodb.utils;

import android.util.LruCache;
import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.RequestQueue;
import com.android.volley.Request;
import android.content.Context;
import com.android.volley.toolbox.Volley;

public class RequestNetwork {

  private static RequestNetwork instance;
  private RequestQueue requestQueue;
  private ImageLoader imageLoader;
  private static Context ctx;

  private RequestNetwork(Context context) {
    ctx = context;
    requestQueue = getRequestQueue();

    imageLoader =
        new ImageLoader(
            requestQueue,
            new ImageLoader.ImageCache() {
              private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

              @Override
              public Bitmap getBitmap(String url) {
                return cache.get(url);
              }

              @Override
              public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
              }
            });
  }

  public static synchronized RequestNetwork getInstance(Context context) {
    if (instance == null) {
      instance = new RequestNetwork(context);
    }
    return instance;
  }

  public RequestQueue getRequestQueue() {
    if (requestQueue == null) {
      // getApplicationContext() is key, it keeps you from leaking the
      // Activity or BroadcastReceiver if someone passes one in.
      requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
    }
    return requestQueue;
  }

  public <T> void addToRequestQueue(Request<T> req) {
    getRequestQueue().add(req);
  }

  public ImageLoader getImageLoader() {
    return imageLoader;
  }
}
