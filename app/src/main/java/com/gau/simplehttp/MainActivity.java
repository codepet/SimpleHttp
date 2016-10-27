package com.gau.simplehttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gau.simplehttp.http.HttpCallback;
import com.gau.simplehttp.http.HttpMethod;
import com.gau.simplehttp.http.Request;
import com.gau.simplehttp.http.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Request.Builder builder = new Request.Builder().url("http://10.0.3.2:8080/TestProject/TestServlet");
                Request request = Request.newRequest(builder);
                try {
                    Response response = request.execute();
                    if (response != null) {
                        if (response.isSuccessful()) {
                            Log.e(TAG, "response is success:" + response.getBody());
                            Log.e(TAG, "response content type:" + response.getContentType());
                        } else {
                            Log.e(TAG, "response is fail:" + response.getBody());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Request.Builder build = new Request.Builder()
                .url("http://10.0.3.2:8080/TestProject/TestServlet")
                .method(HttpMethod.POST)
                .encode("UTF-8")
                .tag("async_request")
                .headers("User-Agent", "guochao-1.0")
                .params("param", "中国")
                .params("username", "aaa")
                .params("password", "bbb")
                .string("{\"string\":\"string\"}");
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
                Log.e(TAG, "onComplete/response:" + response.getBody());
                Log.e(TAG, "onComplete/response: content type=" + response.getContentType());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError:" + e.getMessage());
            }
        }).executeAsync();

    }
}
