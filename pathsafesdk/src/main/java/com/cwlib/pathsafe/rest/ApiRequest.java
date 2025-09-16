package com.cwlib.pathsafe.rest;



import com.codersworld.configs.urls.common.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRequest {

    @GET("ConService.aspx?method=getlockmacdetails")////lockid,contactid,token
    Call<String> getDeviceInfo(@Query("lockid") String lockid,@Query(Constants.P_CONTACT_ID) String contactid,@Query("token") String token);
}
