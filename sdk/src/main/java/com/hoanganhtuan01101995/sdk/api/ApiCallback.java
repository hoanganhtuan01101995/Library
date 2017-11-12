package com.hoanganhtuan01101995.sdk.api;

/**
 * Created by HoangAnhTuan on 8/24/2017.
 */

public interface ApiCallback<T> {
    void onNextPageToken(String nextPageToken);

    void onProgressUpdate(T t);

    void onComplete();
}