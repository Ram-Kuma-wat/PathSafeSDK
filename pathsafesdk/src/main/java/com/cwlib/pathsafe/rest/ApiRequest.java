package com.cwlib.pathsafe.rest;

import com.cwlib.pathsafe.utils.AppUrls;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRequest {

    @GET(AppUrls.PSX_API_BASE_PARAM)
    Call<String> callApi(@Query("paramsval") String param);



}
