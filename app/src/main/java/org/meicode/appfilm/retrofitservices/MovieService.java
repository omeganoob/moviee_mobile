package org.meicode.appfilm.retrofitservices;

import org.meicode.appfilm.model.Genre;
import org.meicode.appfilm.model.Movie;
import org.meicode.appfilm.retrofitresponse.CommentResponse;
import org.meicode.appfilm.retrofitresponse.GenreResponse;
import org.meicode.appfilm.retrofitresponse.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    @GET("movie/all")
    Call<MovieResponse> getMovies();

    @GET("movie/genre/{genre}")
    Call<MovieResponse> getMovieWithGenre(@Path("genre") int genre);

    @GET("movie/age/{age}")
    Call<MovieResponse> getMovieByAge(@Path("age") int age);

    @GET("user/{user}/favorite")
    Call<MovieResponse> getFavorite(@Path("user") long user);

    @GET("genre/list")
    Call<GenreResponse> getGenres();

    @GET("movie/toprated")
    Call<MovieResponse> getTopRated();

    @GET("movie/popular")
    Call<MovieResponse> getPopular();

    @GET("movie/newest")
    Call<MovieResponse> getNewest();

    @GET("movie/onlymovie")
    Call<MovieResponse> getOnlyMovie();

    @GET("tv/all")
    Call<MovieResponse> getTvShows();

    @GET("movie/search")
    Call<MovieResponse> search(@Query("key") String key);

    @GET("movie/{movie}/get/comment")
    Call<CommentResponse> getComments(@Path("movie") Integer movie);

    @FormUrlEncoded
    @POST("movie/{movie}/comment")
    Call<CommentResponse> comment(@Path("movie") Integer movie, @Field("user_id") Integer user_id, @Field("body") String body);
}
