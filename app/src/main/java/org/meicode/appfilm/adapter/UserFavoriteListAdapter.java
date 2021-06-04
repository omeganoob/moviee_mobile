package org.meicode.appfilm.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;
import org.meicode.appfilm.MovieDetailsActivity;
import org.meicode.appfilm.R;
import org.meicode.appfilm.VideoPlayerActivity;
import org.meicode.appfilm.model.Movie;

import java.util.List;

public class UserFavoriteListAdapter extends RecyclerView.Adapter<UserFavoriteListAdapter.Holder> {
    List<Movie> favoriteList;
    Context context;

    public UserFavoriteListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public UserFavoriteListAdapter.Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_list_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserFavoriteListAdapter.Holder holder, int position) {
        Movie movie = favoriteList.get(position);
        holder.Title.setText(movie.getTitle());
        holder.Item.setOnClickListener(v -> {
            Log.d("movieId", String.valueOf(movie.getId()));
            Intent i = new Intent(context, MovieDetailsActivity.class);
            i.putExtra("movieId",movie.getId());
            i.putExtra("movieName",movie.getTitle());
            i.putExtra("movieImageUrl",movie.getPoster());
            i.putExtra("movieFile",movie.getFileUrl());
            i.putExtra("movieDescription",movie.getDescription());
            i.putExtra("movieRate", String.valueOf(movie.getRated()));
            context.startActivity(i);
        });
        String mFileUrl = movie.getFileUrl();
        holder.Start.setOnClickListener(v -> {
            Intent i = new Intent(context, VideoPlayerActivity.class);
            i.putExtra("url",mFileUrl);
            context.startActivity(i);
        });
        Glide.with(context)
                .asBitmap()
                .load(movie.getPoster())
                .centerCrop()
                .into(holder.Poster);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public void setFavoriteList(List<Movie> list) {
        favoriteList = list;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private MaterialCardView Item;
        private ImageView Poster;
        private TextView Title;
        private ImageButton Start;
        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews() {
            Item = itemView.findViewById(R.id.favorite_item);
            Poster = itemView.findViewById(R.id.favorite_item_poster);
            Title = itemView.findViewById(R.id.favorite_item_title);
            Start = itemView.findViewById(R.id.favorite_item_start_btn);
        }
    }
}
