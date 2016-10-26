package com.gau.simplehttp.http;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;

public class HttpAsyncTask extends AsyncTask<Void, Void, Response> {

    private Request request;
    private HttpURLConnection connection;
    private Request.Callback callback;
    private Exception exception;

    public HttpAsyncTask(Request request, HttpURLConnection connection, Request.Callback callback) {
        this.connection = connection;
        this.request = request;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Response doInBackground(Void... params) {
        try {
            if (request != null) {
                return request.execute();
            }
        } catch (IOException e) {
            if (connection != null) {
                connection.disconnect();
            }
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
