package com.gau.simplehttp.http;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.io.IOException;

public class HttpAsyncTask extends AsyncTask<Void, Void, Response> {

    private Request request;
    private Request.Callback callback;
    private Exception exception;

    public HttpAsyncTask(Request request, Request.Callback callback) {
        this.request = request;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (request.getCallbackHandler() == null) {
            Handler handler;
            if (Thread.currentThread() instanceof HandlerThread) {
                handler = new Handler();
            } else {
                handler = new Handler(Looper.getMainLooper());
            }
            request.setCallbackHandler(handler);
        }
    }

    @Override
    protected Response doInBackground(Void... params) {
        try {
            return request.executeAndWait();
        } catch (IOException e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        if (response == null) {
            if (callback != null) {
                if (exception != null) {
                    callback.onError(exception);
                } else {
                    callback.onError(new Exception("UnKnown Exception"));
                }
            }
        } else {
            if (callback != null) {
                callback.onComplete(response);
            }
        }
    }
}
