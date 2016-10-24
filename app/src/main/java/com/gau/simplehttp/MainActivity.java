package com.gau.simplehttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gau.simplehttp.http.Logger;
import com.gau.simplehttp.http.Request;
import com.gau.simplehttp.http.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Request.Builder builder = new Request.Builder().url("https://www.baidu.com");
        Request request = Request.newRequest(builder, new Request.Callback() {
            @Override
            public void onComplete(Response response) {
                Logger.e("Main", "onComplete");
            }

            @Override
            public void onError(Throwable e) {
                Logger.e("Main", "onError:" + e.getMessage());
            }
        });
        request.execute();
    }
}
