package com.rex.db.node.example;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.rex.hellodb.HelloDB;
import com.rex.hellodb.events.listeners.QueryEventListener;
import com.rex.hellodb.query.Query;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
  private TextView textView;
  private HelloDB db = HelloDB.getInstance();
  private Query users = db.getReference("users");
  private QueryEventListener query;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    textView = (TextView) findViewById(R.id.textView);

    final EditText key = (EditText) findViewById(R.id.key);
    final EditText value = (EditText) findViewById(R.id.value);
    final Button button1 = (Button) findViewById(R.id.button1);
    final Button button2 = (Button) findViewById(R.id.button2);
    final Button button3 = (Button) findViewById(R.id.button3);
    final Button button4 = (Button) findViewById(R.id.button4);

    textView.setOnClickListener(
        (v) -> {
          ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE))
              .setPrimaryClip(ClipData.newPlainText("clipboard", textView.getText().toString()));
          Toast.makeText(this, "Text copied", Toast.LENGTH_SHORT).show();
        });

    query =
        new QueryEventListener() {
          @Override
          public void onDataChanged(String str) {
            textView.setText(str);
          }

          @Override
          public void onDataCancelled(Exception e) {
            textView.setText(e.toString());
          }
        };

    try {
      users.orderBy("age", false).limit(3).get().addOnQueryEventListener(query);
    } catch (JSONException e) {
    }

    button1.setText("Update");
    button1.setOnClickListener((v) -> {});
    button2.setText("Add");
    button2.setOnClickListener(
        (v) -> {
          JSONObject map = new JSONObject();
          try {
            map.put("name", key.getText().toString());
            map.put("email", value.getText().toString());
            map.put("age", "20");
          } catch (JSONException e) {
          }
          users.insert(map);
        });
    button3.setText("Remove");
    button3.setOnClickListener((v) -> {});
    button4.setText("Get");
    button4.setOnClickListener(
        (v) -> {
          try {
            users.orderBy("age", false).limit(3).get().addOnQueryEventListener(query);
          } catch (JSONException e) {
          }
        });
  }
}
;
