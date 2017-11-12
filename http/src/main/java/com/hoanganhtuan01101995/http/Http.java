package com.hoanganhtuan01101995.http;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HoangAnhTuan on 8/24/2017.
 */

public class Http {

    Map<String, String> body = new HashMap<>();
    Map<String, String> header = new HashMap<>();
    String base_url;
    String url;
    HttpCallBack callBack;
    HttpMethod method = HttpMethod.GET;

    public static Builder build() {
        return new Builder();
    }

    public static class Builder {

        private Http http;

        public Http execute() {
            http.execute();
            return http;
        }

        private Builder() {
            http = new Http();
        }

        public Builder putHeader(String key, String value) {
            if (value == null || value.isEmpty()) return this;

            this.http.header.put(key, value);
            return this;
        }

        public Builder putParameter(String key, String value) {
            if (value == null || value.isEmpty()) return this;

            this.http.body.put(key, value);
            return this;
        }

        public Builder setBase_url(String base_url) {
            if (base_url == null || base_url.isEmpty())
                throw new RuntimeException("base_url not empty");

            this.http.base_url = base_url;
            return this;
        }

        public Builder setUrl(String url) {
            if (url == null || url.isEmpty()) throw new RuntimeException("url not empty");

            this.http.url = url;
            return this;
        }

        public Builder setMethod(HttpMethod method) {
            if (method == null) throw new RuntimeException("method not null");
            this.http.method = method;
            return this;
        }

        public Builder withCallBack(HttpCallBack callBack) {
            this.http.callBack = callBack;
            return this;
        }

    }

    private void execute() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HttpService service = retrofit.create(HttpService.class);
        Call<ResponseBody> call;
        switch (method) {
            case GET:
                call = service.getDataByMethodGet(url, header, body);
                break;
            case POST:
                call = service.getDataByMethodPost(url, header, body);
                break;
            default:
                call = service.getDataByMethodPost(url, header, body);
        }
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
                if (callBack == null) return;
                try {
                    if (response.body() != null) {
                        callBack.onSuccess(response.body().string());
                    } else if (response.errorBody() != null) {
                        callBack.onFailure(response.errorBody().string());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callBack.onCompleted();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull final Throwable t) {
                if (callBack == null) return;
                callBack.onFailure(t.toString());
                callBack.onCompleted();
            }
        });
    }

}

