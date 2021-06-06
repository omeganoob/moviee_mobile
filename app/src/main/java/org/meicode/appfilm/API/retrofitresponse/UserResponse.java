package org.meicode.appfilm.API.retrofitresponse;

import com.google.gson.annotations.SerializedName;

import org.meicode.appfilm.Models.User;

public class UserResponse {

    @SerializedName("success")
    private Boolean success;

    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private User user = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
