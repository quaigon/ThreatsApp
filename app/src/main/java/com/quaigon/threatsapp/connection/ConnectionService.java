package com.quaigon.threatsapp.connection;

import com.quaigon.threatsapp.pojo.Status;
import com.quaigon.threatsapp.pojo.Token;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Kamil on 12.03.2016.
 */
public interface ConnectionService {
    @FormUrlEncoded
    @POST("/TrafficThreat/rest/login/")
    Call<Token> login(@Field("login") String login, @Field("password") String password );

    @FormUrlEncoded
    @POST("/TrafficThreat/rest/addThreat/")
    Call<Status> addThreat(@Field("typeOfThreat") String type,
                          @Field("description") String description,
                          @Field("coordinates") String coords,
                          @Field("token") String token);

}
