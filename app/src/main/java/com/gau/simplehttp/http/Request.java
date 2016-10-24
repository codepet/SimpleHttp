package com.gau.simplehttp.http;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    private final static int CONNECT_TIME_OUT_MILLISECOND = 10000;
    private final static int READ_TIME_OUT_MILLISECOND = 20000;

    private String url;
    private Map<String, String> params;
    private List<String> headers;
    private HttpMethod method;

    private HttpURLConnection connection;
    private Handler handler;
    private static Callback mCallback;

    private Request(Builder builder) {
        this.url = builder.url;
        this.params = builder.params;
        this.headers = builder.headers;
        this.method = builder.method;
    }

    public static Request newRequest(Builder builder) {
        return newRequest(builder, null);
    }

    public static Request newRequest(Builder builder, Callback callback) {
        mCallback = callback;
        return new Request(builder);
    }

    public void execute() {
        HttpAsyncTask asyncTask = new HttpAsyncTask(this, mCallback);
        asyncTask.execute();
    }

    Response executeAndWait() throws IOException {
        buildConnection();
        return response(connection);
    }

    private void buildConnection() throws IOException {
        URL requestUrl = new URL(url);
        connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod(String.valueOf(method));
        connection.setConnectTimeout(CONNECT_TIME_OUT_MILLISECOND);
        connection.setReadTimeout(READ_TIME_OUT_MILLISECOND);
        connection.setDoInput(true);
        connection.setUseCaches(true);
        connection.connect();
    }

    private Response response(HttpURLConnection connection) throws IOException {
        return new Response.Builder()
                .code(connection.getResponseCode())
                .message(connection.getResponseMessage())
                .method(connection.getRequestMethod())
                .contentType(connection.getContentType())
                .body(getResponseBody(connection.getInputStream()))
                .build();
    }

    private String getResponseBody(InputStream stream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    Handler getCallbackHandler() {
        return handler;
    }

    void setCallbackHandler(Handler handler) {
        this.handler = handler;
    }

    public interface Callback {
        void onComplete(Response response);

        void onError(Throwable e);
    }

    public static class Builder {

        private String url;
        private Map<String, String> params = new HashMap<>();
        private List<String> headers = new ArrayList<>();
        private HttpMethod method = HttpMethod.GET;

        public Builder url(String url) {
            if (url == null || url.isEmpty()) {
                throw new IllegalArgumentException("url can not be null or empty");
            }
            this.url = url;
            return this;
        }

        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder params(String key, String value) {
            params.put(key, value);
            return this;
        }

        public Builder headers(String key, String values) {
            headers.add(key);
            headers.add(values);
            return this;
        }

        public Request build() {
            return new Request(this);
        }

    }

}
