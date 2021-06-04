package org.meicode.appfilm.retrofitresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.meicode.appfilm.model.Movie;

import java.util.List;

public class MovieResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName(("user_id"))
    @Expose
    private int user_id;
    @SerializedName("movies")
    @Expose
    private List<Movie> movies = null;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
