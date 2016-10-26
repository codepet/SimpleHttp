package com.gau.simplehttp.http;

public interface HttpCallback {
    void onComplete(Response response);

    void onError(Throwable e);
}