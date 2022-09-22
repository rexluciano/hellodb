package com.rex.hellodb;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.rex.hellodb.HelloDB;
import com.rex.hellodb.ResourcesHelper;
import com.rex.hellodb.components.ComponentDiscoveryService;
import com.rex.hellodb.components.ComponentRegistrar;
import com.rex.hellodb.query.Query;
import java.util.MissingResourceException;

public class HelloDB {

  private static HelloDB db;
  protected static Context context;
  private static HelloDB.Config config;
  private static String serverUrl;
  private static String apiKey;
  private static String secretKey;
  private static String ref;
  private static String sdkVersion;
  private static ComponentRegistrar reg;
  private static ComponentDiscoveryService cds;

  public HelloDB() {
    if (context != null) {
      if (config != null) {
        String dbUrl = config.getServerUrl();
        String pId = config.getProjectId();
        apiKey = config.getApiKey();
        secretKey = config.getClientKey();
        serverUrl =
            "https://"
                + dbUrl.replace("https://", "").replace("http://", "")
                + "/v1/project/"
                + pId
                + "/";
      }
    }
  }

  private static ServiceConnection sc =
      new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
          if (name.getClass().equals(ComponentDiscoveryService.class)) {
            reg = (ComponentRegistrar) service;
            cds = reg.getService();
            sdkVersion = cds.getSDKVersion();
          }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
          Log.i(
              this.getClass().getSimpleName(),
              "onServiceDisconnected: "
                  + name.getClassName()
                  + " was disconnected from the service.");
        }
      };

  public interface Configuration {
    String SERVER_URL = "hellodb_url",
        PROJECT_ID = "hellodb_project_id",
        USERNAME = "hellodb_username",
        PASSWORD = "hellodb_password";
  }

  public static void initialize(Context ctx) {
    context = ctx;
    try {
      HelloDB.Config cfg = new HelloDB.Config();
      cfg.serverUrl(ResourcesHelper.getStringResourceByKey(ctx, Configuration.SERVER_URL));
      cfg.projectId(ResourcesHelper.getStringResourceByKey(ctx, Configuration.PROJECT_ID));
      cfg.username(ResourcesHelper.getStringResourceByKey(ctx, Configuration.SERVER_URL));
      cfg.password(ResourcesHelper.getStringResourceByKey(ctx, Configuration.PASSWORD));
      cfg.build();
    } catch (Resources.NotFoundException e) {
      throw new MissingResourceException(e.toString(), "", "");
    }
    context.bindService(
        new Intent(context, ComponentDiscoveryService.class), sc, Context.BIND_AUTO_CREATE);
  }

  public static void initialize(Context ctx, HelloDB.Config cfg) {
    context = ctx;
    config = cfg;
    context.bindService(
        new Intent(context, ComponentDiscoveryService.class), sc, Context.BIND_AUTO_CREATE);
  }

  public static HelloDB getInstance() {
    if (db == null) {
      db = new HelloDB();
    }
    return db;
  }

  public Query getReference(String ref) {
    this.ref = ref;
    return new Query(ref);
  }

  protected String getServerUrl() {
    return serverUrl;
  }

  protected String getUsername() {
    return apiKey;
  }

  protected String getPassword() {
    return secretKey;
  }

  protected Context getContext() {
    return context;
  }

  public String getSdkVersion() {
    return sdkVersion == null ? "1.0.0" : sdkVersion;
  }

  public static class Config {

    private String url;
    private String id;
    private String key;
    private String secretKey;

    public Config serverUrl(String url) {
      this.url = url;
      return this;
    }

    public Config projectId(String id) {
      this.id = id;
      return this;
    }

    public Config username(String key) {
      this.key = key;
      return this;
    }

    public Config password(String key) {
      this.secretKey = key;
      return this;
    }

    private String getServerUrl() {
      return url;
    }

    private String getProjectId() {
      return id;
    }

    private String getApiKey() {
      return key;
    }

    private String getClientKey() {
      return secretKey;
    }

    public Config build() {
      if (url == null) {
        throw new NullPointerException("HelloDB: Invalid server url");
      } else if (id == null) {
        throw new NullPointerException("HelloDB: Invalid project id");
      }
      return this;
    }
  }
}
