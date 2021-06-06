package org.meicode.appfilm.Utils;

import org.meicode.appfilm.API.ClientExecutor;

import retrofit2.Retrofit;

public class AppConstraint {
    public static final String baseURL = "http://192.168.56.1/moviee/public/api/v1/";
    //Movie Service
    public static final Integer getMovies = 1;
    public static final Integer getMovieWithGenre = 2;
    public static final Integer getMovieByAge = 3;
    public static final Integer getFavorite = 4;
    public static final Integer getGenres = 5;
    public static final Integer getTopRated = 6;
    public static final Integer getPopular = 7;
    public static final Integer getNewest = 8;
    public static final Integer getOnlyMovie = 9;
    public static final Integer getTvShows = 10;
    public static final Integer search = 11;
    public static final Integer getComments = 12;
    public static final Integer comment = 13;

    //User Service
    public static final Integer login = 101;
    public static final Integer register = 102;
    public static final Integer isfav = 103;
    public static final Integer addtofav = 104;

    public static final Retrofit retrofit = ClientExecutor.getInstance().retrofit;

}
