package com.hoanganhtuan01101995.http;

/**
 * Created by Hoang Anh Tuan on 11/1/2017.
 */

public abstract class HttpCallBack {
    public abstract void onSuccess(String data);

    public void onFailure(String message) {
    }

    public void onCompleted() {
    }
}
