package com.quaigon.threatsapp.connection;

import com.quaigon.threatsapp.dto.Status;
import com.quaigon.threatsapp.dto.Threat;
import com.quaigon.threatsapp.dto.ThreatType;
import com.quaigon.threatsapp.dto.Token;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Kamil on 12.03.2016.
 */
public interface ConnectionService {
    @FormUrlEncoded
    @POST("/TrafficThreat/rest/login/")
    Call<Token> login(@Field("login") String login, @Field("password") String password);

    @FormUrlEncoded
    @POST("/TrafficThreat/rest/addThreat/")
    Observable<Status> addThreat(@Field("typeOfThreat") String type,
                                 @Field("description") String description,
                                 @Field("coordinates") String coords,
                                 @Field("location") String location,
                                 @Field("token") String token);

    @GET("/TrafficThreat/rest/getThreats/")
    Observable<List<Threat>> getThreats();

    @Multipart
    @POST("/TrafficThreat/rest/addImage/")
    Observable<Status> addImage(@Query("uuid") String uuid, @Part() MultipartBody.Part file);

    @FormUrlEncoded
    @POST("/TrafficThreat/rest/addVote/")
    Observable<Status> addVote( @Field("stars") String starsNo,
                                @Field("uuid") String threatUuid,
                               @Field("login") String userLogin,
                               @Field("comment") String comment,
                               @Field("token") String token);

    @GET("/TrafficThreat/rest/getThreatTypes/")
    Observable<List<ThreatType>> getThreatTypes();

}
