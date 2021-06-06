package org.meicode.appfilm.API.retrofitresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckFavoriteResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("isfav")
    @Expose
    private Boolean isfav;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getIsfav() {
        return isfav;
    }

    public void setIsfav(Boolean isfav) {
        this.isfav = isfav;
    }
}
