package com.gau.simplehttp.http;

public class Response {

    private final int code;
    private final String message;
    private final String method;
    private final String contentType;
    private final String body;

    public Response(Builder builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.method = builder.method;
        this.contentType = builder.contentType;
        this.body = builder.body;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    public static class Builder {
        private int code;
        private String message;
        private String method;
        private String contentType;
        private String body;

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Response build() {
            return new Response(this);
        }

    }

}
