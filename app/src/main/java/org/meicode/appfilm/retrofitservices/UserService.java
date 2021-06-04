package org.meicode.appfilm.retrofitservices;

import org.meicode.appfilm.retrofitresponse.AddFavResponse;
import org.meicode.appfilm.retrofitresponse.CheckFavoriteResponse;
import org.meicode.appfilm.retrofitresponse.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @FormUrlEncoded
    @POST("login")
    Call<UserResponse> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<UserResponse> register(@Field("name") String name, @Field("email") String email, @Field("password") String password);

    @GET("isfav")
    Call<CheckFavoriteResponse> isfav(@Query("user_id") Integer user_id, @Query("movie_id") Integer movie_id);

    @FormUrlEncoded
    @POST("addtofav")
    Call<AddFavResponse> addtofav(@Field("user_id") Integer user_id, @Query("movie_id") Integer movie_id);
}
