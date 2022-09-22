package com.rex.hellodb.query;

import android.util.Base64;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.rex.hellodb.HelloDB;
import com.rex.hellodb.events.listeners.QueryEventListener;
import com.rex.hellodb.query.Query;
import com.rex.hellodb.utils.RequestNetwork;
import com.rex.hellodb.exceptions.DatabaseException;
import com.rex.worker.ThreadWorker;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.rex.worker.Worker;

public class Query extends HelloDB {

  private static final String TAG = Query.class.getSimpleName();
  private RequestNetwork net;
  private QueryEventListener listener;
  private String child;
  private String ref;
  private String url;
  private JSONObject data;
  private Worker worker;

  public Query(String ref) {
    this.ref = ref;
    this.url = getServerUrl() + ref;
    net = RequestNetwork.getInstance(getContext());
    data = new JSONObject();
    worker = new Worker();
  }

  public Query insert(JSONObject map) {
    if (net != null) {
      worker
          .create(context)
          .work(
              new ThreadWorker() {
                @Override
                public void onWork() {
                  Log.i(TAG, "onWork: Preparing documents.");
                  insertDocument(map);
                }

                @Override
                public void onProgress() {
                  Log.i(TAG, "onProgress: Saving data to the documents.");
                }

                @Override
                public void onDone() {
                  Log.i(TAG, "onDone: Saving document done.");
                  worker.stop();
                }
              })
          .start();
    }
    return this;
  }

  public Query update(JSONObject map) {
    if (net != null) {
      worker
          .create(context)
          .work(
              new ThreadWorker() {
                @Override
                public void onWork() {
                  Log.i(TAG, "onWork: Preparing documents.");
                  updateDocument(map);
                }

                @Override
                public void onProgress() {
                  Log.i(TAG, "onProgress: Updating data from the documents.");
                }

                @Override
                public void onDone() {
                  Log.i(TAG, "onDone: Updating document done.");
                  worker.stop();
                }
              })
          .start();
    }
    return this;
  }

  public Query get() {
    if (net != null) {
      worker
          .create(context)
          .work(
              new ThreadWorker() {
                @Override
                public void onWork() {
                  Log.i(TAG, "onWork: Preparing documents.");
                  getDocument();
                }

                @Override
                public void onProgress() {
                  Log.i(TAG, "onProgress: Getting data from the documents.");
                }

                @Override
                public void onDone() {
                  Log.i(TAG, "onDone: Getting document done.");
                  worker.stop();
                }
              })
          .start();
    }
    return this;
  }

  public Query delete() {
    if (net != null) {
      worker
          .create(context)
          .work(
              new ThreadWorker() {
                @Override
                public void onWork() {
                  Log.i(TAG, "onWork: Preparing documents.");
                  deleteDocument();
                }

                @Override
                public void onProgress() {
                  Log.i(TAG, "onProgress: Deleting data from the documents.");
                }

                @Override
                public void onDone() {
                  Log.i(TAG, "onDone: Deleting document done.");
                  worker.stop();
                }
              })
          .start();
    }
    return this;
  }

  private void insertDocument(JSONObject map) {
    final String requestBody = map.toString();
    StringRequest strReq =
        new StringRequest(
            Request.Method.POST,
            url + "/insert",
            response -> {
              if (listener != null) {
                try {
                  listener.onDataChanged(response.toString());
                } catch (Exception e) {
                  listener.onDataCancelled(e);
                }
              }
            },
            error -> {
              if (listener != null) {
                listener.onDataCancelled(
                    new DatabaseException("Query error: " + error.getLocalizedMessage()));
              }
            }) {

          @Override
          public String getBodyContentType() {
            return "application/json; charset=utf-8";
          }

          @Override
          public byte[] getBody() throws AuthFailureError {
            try {
              return requestBody == null ? null : requestBody.getBytes("utf-8");
            } catch (UnsupportedEncodingException uee) {
              VolleyLog.wtf(
                  "Unsupported Encoding while trying to get the bytes of %s using %s",
                  requestBody, "utf-8");
              return null;
            }
          }

          @Override
          public Map<String, String> getHeaders() throws AuthFailureError {
            String credentials = getUsername() + ":" + getPassword();
            String base64EncodedCredentials =
                Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("User-Agent", "HelloDB Android");
            headers.put("Authorization", "Basic " + base64EncodedCredentials);
            return headers;
          }
        };
    net.addToRequestQueue(strReq);
  }

  private void updateDocument(JSONObject map) {
    final String requestBody = map.toString();
    StringRequest strReq =
        new StringRequest(
            Request.Method.POST,
            url + "/update",
            response -> {
              if (listener != null) {
                try {
                  listener.onDataChanged(response.toString());
                } catch (Exception e) {
                  listener.onDataCancelled(e);
                }
              }
            },
            error -> {
              if (listener != null) {
                listener.onDataCancelled(
                    new DatabaseException("Query error: " + error.getLocalizedMessage()));
              }
            }) {

          @Override
          public String getBodyContentType() {
            return "application/json; charset=utf-8";
          }

          @Override
          public byte[] getBody() throws AuthFailureError {
            try {
              return requestBody == null ? null : requestBody.getBytes("utf-8");
            } catch (UnsupportedEncodingException uee) {
              VolleyLog.wtf(
                  "Unsupported Encoding while trying to get the bytes of %s using %s",
                  requestBody, "utf-8");
              return null;
            }
          }

          @Override
          public Map<String, String> getHeaders() throws AuthFailureError {
            String credentials = getUsername() + ":" + getPassword();
            String base64EncodedCredentials =
                Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("User-Agent", "HelloDB Android");
            headers.put("Authorization", "Basic " + base64EncodedCredentials);
            return headers;
          }
        };
    net.addToRequestQueue(strReq);
  }

  private void getDocument() {
    final String requestBody = data.toString();
    StringRequest strReq =
        new StringRequest(
            Request.Method.POST,
            url + "/get",
            response -> {
              if (listener != null) {
                try {
                  listener.onDataChanged(response.toString());
                } catch (Exception e) {
                  listener.onDataCancelled(e);
                }
              }
            },
            error -> {
              if (listener != null) {
                listener.onDataCancelled(
                    new DatabaseException("Query error: " + error.getLocalizedMessage()));
              }
            }) {
          @Override
          public String getBodyContentType() {
            return "application/json; charset=utf-8";
          }

          @Override
          public byte[] getBody() throws AuthFailureError {
            try {
              return requestBody == null ? null : requestBody.getBytes("utf-8");
            } catch (UnsupportedEncodingException uee) {
              VolleyLog.wtf(
                  "Unsupported Encoding while trying to get the bytes of %s using %s",
                  requestBody, "utf-8");
              return null;
            }
          }

          @Override
          public Map<String, String> getHeaders() throws AuthFailureError {
            String credentials = getUsername() + ":" + getPassword();
            String base64EncodedCredentials =
                Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("User-Agent", "HelloDB Android");
            headers.put("Authorization", "Basic " + base64EncodedCredentials);
            return headers;
          }
        };
    net.addToRequestQueue(strReq);
  }

  private void deleteDocument() {
    final String requestBody = data.toString();
    StringRequest strReq =
        new StringRequest(
            Request.Method.POST,
            url + "/delete",
            response -> {
              if (listener != null) {
                try {
                  listener.onDataChanged(response.toString());
                } catch (Exception e) {
                  listener.onDataCancelled(e);
                }
              }
            },
            error -> {
              if (listener != null) {
                listener.onDataCancelled(
                    new DatabaseException("Query error: " + error.getLocalizedMessage()));
              }
            }) {
          @Override
          public String getBodyContentType() {
            return "application/json; charset=utf-8";
          }

          @Override
          public byte[] getBody() throws AuthFailureError {
            try {
              return requestBody == null ? null : requestBody.getBytes("utf-8");
            } catch (UnsupportedEncodingException uee) {
              VolleyLog.wtf(
                  "Unsupported Encoding while trying to get the bytes of %s using %s",
                  requestBody, "utf-8");
              return null;
            }
          }

          @Override
          public Map<String, String> getHeaders() throws AuthFailureError {
            String credentials = getUsername() + ":" + getPassword();
            String base64EncodedCredentials =
                Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("User-Agent", "HelloDB Android");
            headers.put("Authorization", "Basic " + base64EncodedCredentials);
            return headers;
          }
        };
    net.addToRequestQueue(strReq);
  }

  public Query limit(int limit) throws JSONException {
    data.put("limit", Integer.toString(limit));
    return this;
  }

  public Query orderBy(String ob, boolean isDesc) throws JSONException {
    data.put("orderBy", ob);
    data.put("desc", isDesc);
    return this;
  }

  public Query whereEqualTo(String where, String equalTo) throws JSONException {
    data.put("where", where);
    data.put("equalTo", equalTo);
    return this;
  }

  public Query document(int id) throws JSONException {
    data.put("id", id);
    return this;
  }

  public Query document(String id) throws JSONException {
    data.put("id", id);
    return this;
  }

  public void addOnQueryEventListener(QueryEventListener listener) {
    this.listener = listener;
  }
}
