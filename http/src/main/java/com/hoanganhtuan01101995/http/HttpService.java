package com.hoanganhtuan01101995.http;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Hoang Anh Tuan on 11/1/2017.
 */

interface HttpService {
    @POST("{url}")
    Call<ResponseBody> getDataByMethodPost(@Path("url") String url, @HeaderMap Map<String, String> header, @Body Map<String, String> body);

    @GET("{url}")
    Call<ResponseBody> getDataByMethodGet(@Path("url") String url, @HeaderMap Map<String, String> header, @QueryMap Map<String, String> body);
}
