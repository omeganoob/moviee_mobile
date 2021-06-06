package org.meicode.appfilm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.meicode.appfilm.R;
import org.meicode.appfilm.Models.Comment;

import java.util.List;

public class CommentSectionAdapter extends RecyclerView.Adapter<CommentSectionAdapter.Holder> {
    Context context;
    List<Comment> comments;

    public CommentSectionAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public CommentSectionAdapter.Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentSectionAdapter.Holder holder, int position) {
        Comment comment = comments.get(position);
        holder.commentUser.setText(comment.getUserName());
        holder.commentBody.setText(comment.getBody());
        holder.commentTime.setText(comment.getCreatedAt().split("T")[0]);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(List<Comment> list) {
        comments = list;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView commentUser, commentBody, commentTime;
        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews() {
            commentBody = itemView.findViewById(R.id.commentBody);
            commentUser = itemView.findViewById(R.id.commentUser);
            commentTime = itemView.findViewById(R.id.commentTime);
        }
    }
}
