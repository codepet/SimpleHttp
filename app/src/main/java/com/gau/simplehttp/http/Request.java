package com.gau.simplehttp.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Request {

    private final static int CONNECT_TIME_OUT_MILLISECOND = 10000;
    private final static int READ_TIME_OUT_MILLISECOND = 20000;

    private String url;
    private List<String> params;
    private List<String> headers;
    private String json;
    private HttpMethod method;

    private HttpURLConnection connection;
    private static Callback mCallback;

    private Request(Builder builder) {
        this.url = builder.url;
        this.params = builder.params;
        this.headers = builder.headers;
        this.method = builder.method;
        this.json = builder.json;
    }

    public static Request newRequest(Builder builder) {
        return newRequest(builder, null);
    }

    public static Request newRequest(Builder builder, Callback callback) {
        mCallback = callback;
        return new Request(builder);
    }

    public void executeAsync() {
        HttpAsyncTask asyncTask = new HttpAsyncTask(this, connection, mCallback);
        asyncTask.execute();
    }

    public Response execute() throws IOException {
        if (method == HttpMethod.POST) {
            post();
        } else {
            get();
        }
        return response(connection);
    }

    private void get() throws IOException {
        URL requestUrl = new URL(getUrl(url));
        connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod(String.valueOf(method));
        connection.setConnectTimeout(CONNECT_TIME_OUT_MILLISECOND);
        connection.setReadTimeout(READ_TIME_OUT_MILLISECOND);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connectHeaders();
        connection.connect();
    }

    private String getUrl(String url) {
        StringBuilder sb = new StringBuilder();
        sb.append(url).append("?");
        for (int i = 0; i < params.size(); i += 2) {
            sb.append(params.get(i));
            sb.append("=");
            sb.append(params.get(i + 1));
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private void post() throws IOException {
        URL requestUrl = new URL(url);
        connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod(String.valueOf(method));
        connection.setConnectTimeout(CONNECT_TIME_OUT_MILLISECOND);
        connection.setReadTimeout(READ_TIME_OUT_MILLISECOND);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connectHeaders();
        connection.connect();
        postBody();
    }

    private void postBody() throws IOException {
        postForm();
        postString();
    }

    private void postForm() throws IOException {
        if (params != null && params.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.size(); i += 2) {
                sb.append(params.get(i));
                sb.append("=");
                sb.append(params.get(i + 1));
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            connection.getOutputStream().write(sb.toString().getBytes());
        }
    }

    private void postString() throws IOException {
        if (json != null && !json.isEmpty()) {
            connection.getOutputStream().write(json.getBytes());
        }
    }

    private void connectHeaders() {
        if (headers != null && headers.size() > 0) {
            for (int i = 0; i < headers.size(); i += 2) {
                connection.setRequestProperty(headers.get(i), headers.get(i + 1));
            }
        }
    }

    private Response response(HttpURLConnection connection) throws IOException {
        Response resp = new Response.Builder()
                .code(connection.getResponseCode())
                .message(connection.getResponseMessage())
                .method(connection.getRequestMethod())
                .contentType(connection.getContentType())
                .contentLength(connection.getContentLength())
                .body(getResponseBody(connection.getInputStream()))
                .build();
        connection.disconnect();
        return resp;
    }

    private String getResponseBody(InputStream stream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public enum HttpMethod {
        GET, POST
    }

    public interface Callback {
        void onComplete(Response response);

        void onError(Throwable e);
    }

    public static class Builder {

        private String url;
        private List<String> params = new ArrayList<>();
        private List<String> headers = new ArrayList<>();
        private HttpMethod method = HttpMethod.GET;
        private String json;

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
            params.add(key);
            params.add(value);
            return this;
        }

        public Builder headers(String key, String values) {
            headers.add(key);
            headers.add(values);
            return this;
        }

        public Builder json(String json) {
            this.json = json;
            return this;
        }

        public Request build() {
            return new Request(this);
        }

    }

}
