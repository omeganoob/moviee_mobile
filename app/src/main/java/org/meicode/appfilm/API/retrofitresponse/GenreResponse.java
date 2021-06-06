package org.meicode.appfilm.API.retrofitresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.meicode.appfilm.Models.Genre;

import java.util.List;

public class GenreResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("genre")
    @Expose
    private List<Genre> genre = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Genre> getGenre() {
        return genre;
    }

    public void setGenre(List<Genre> genre) {
        this.genre = genre;
    }
}
