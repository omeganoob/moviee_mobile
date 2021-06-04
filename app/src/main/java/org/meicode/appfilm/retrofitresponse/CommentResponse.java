package org.meicode.appfilm.retrofitresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.meicode.appfilm.model.Comment;

import java.util.List;

public class CommentResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
