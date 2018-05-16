package com.example.lado.banksystem;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by lado on 25/3/18.
 */

public class BitcoinWatcherActivity extends AppCompatActivity {
    public static final String BPI_ENDPOINT = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private ProgressDialog progressDialog;
    private TextView txt;
    OkHttpClient okHttpClient = new OkHttpClient();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curencybitcoin);
        txt = (TextView) findViewById(R.id.txt);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Wait...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_load) {
            load();
        }
        return super.onOptionsItemSelected(item);
    }

    private void load() {
        Request request = new Request.Builder().url(BPI_ENDPOINT).build();
        progressDialog.show();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(BitcoinWatcherActivity.this, "Error "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String body = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        parseBriResponse(body);
                    }
                });
            }
        });
    }

    private void parseBriResponse(String body) {
        try {
            StringBuilder builder = new StringBuilder();
            JSONObject jsonObject = new JSONObject(body);
            JSONObject timeObject = jsonObject.getJSONObject("time");
            builder.append(timeObject.getString("updated ")).append("\n\n");

            JSONObject bpiObject = jsonObject.getJSONObject("bpi");
            JSONObject usdObject = jsonObject.getJSONObject("USD");
            builder.append(usdObject.getString("rate ")).append("$").append("\n");

            JSONObject gpbObject = bpiObject.getJSONObject("GBP");
            builder.append(gpbObject.getString("rate ")).append("E").append("\n");

            JSONObject eurObject = bpiObject.getJSONObject("EUR");
            builder.append(gpbObject.getString("rate ")).append("EUR").append("\n");

            txt.setText(builder.toString());
        }
        catch (Exception e) {

        }
    }
}
