package com.example.okhttp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    //private TextView jsonTextView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //jsonTextView= (TextView) findViewById(R.id.jsonText);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

         listItems = new ArrayList<>();

        String jsonURL = "https://jsonplaceholder.typicode.com/users";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(jsonURL)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String json= response.body().string();
                try {
                    getJsonFromString(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void getJsonFromString(String json) throws JSONException {
        JSONArray jsonArray= new JSONArray(json);
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            final String name = jsonObject.getString("name");
            final String username = jsonObject.getString("username");
            final String email = jsonObject.getString("email");
            //Log.i("name",name);
            ListItem listItem= new ListItem(
                    "Name:"+name,"Username:"+username,"Email:"+email
            );

            listItems.add(listItem);
            //jsonTextView.setText("name:"+name+"\nusername:"+username+"\nemail:"+email);

            //displayJsonInformation(jsonObject);
        }
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new MyAdapter(listItems,MainActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });
    }

 /*   private void displayJsonInformation(JSONObject jsonObject) throws JSONException {

        final String name = jsonObject.getString("name");
        final String username = jsonObject.getString("username");
        final String email = jsonObject.getString("email");
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListItem listItem= new ListItem(
                        "Name:"+name,"Username:"+username,"Email:"+email
                );

                listItems.add(listItem);
                //jsonTextView.setText("name:"+name+"\nusername:"+username+"\nemail:"+email);
            }
        });

    }*/
}
