package com.example.sherlock.fixmyeyes;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface API {

    @Multipart
    @POST("upload_and_edit")
    Call<response_POJO> send(@Part MultipartBody.Part image, @Part("fb_id") RequestBody fbid);

    @Multipart
    @POST("upload_src")
    Call<response_POJO> send_src(@Part MultipartBody.Part image, @Part("fb_id") RequestBody fbid);

}
