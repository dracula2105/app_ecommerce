package com.example.enamul.serviceClient;


import com.example.enamul.model.InforUser;
import com.example.enamul.model.ResponseLogin;
import com.example.enamul.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {
//    @POST("user")
//    Call<User> postDetails(@Body User post);
//    @GET("todos/1")
//    Call<String> get();
    @POST("auth/login")
    Call<ResponseLogin> login(@Body User user);
//
//    @PUT("userlog")
//    Call<User> putDetails(@Body User put);
//    //here we return response body. the reason for this is so that we can turn the whole response object to string the later
//    //extract values from it using JSONObject.
    @GET("getcurrentuser")
    Call<InforUser> getUsers(@Header("Authorization") String token);
}
