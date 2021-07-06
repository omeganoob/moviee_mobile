package org.meicode.appfilm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.meicode.appfilm.Activity.MovieDetailsActivity;
import org.meicode.appfilm.R;
import org.meicode.appfilm.Models.Movie;

import java.util.List;

public class SearchResultListAdapter extends RecyclerView.Adapter<SearchResultListAdapter.Holder> {

    private Context context;
    private List<Movie> resultList;

    public SearchResultListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public SearchResultListAdapter.Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchResultListAdapter.Holder holder, int position) {
        Movie movie = resultList.get(position);
        holder.itemTitle.setText(movie.getTitle());

        Glide.with(context)
                .asBitmap()
                .load(movie.getPoster())
                .centerCrop()
                .into(holder.itemImage);
        holder.itemImage.setOnClickListener(v -> {
            Intent i = new Intent(context, MovieDetailsActivity.class);
            i.putExtra("movieId",movie.getId());
            i.putExtra("movieName",movie.getTitle());
            i.putExtra("movieImageUrl",movie.getPoster());
            i.putExtra("movieFile",movie.getFileUrl());
            i.putExtra("movieDescription",movie.getDescription());
            i.putExtra("movieRate", String.valueOf(movie.getRated()));
            context.startActivity(i);
        });
    }

    public int getPosition() {
        return this.getPosition();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public void setResultList(List<Movie> list) {
        resultList = list;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemTitle;
        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews() {
            itemImage = itemView.findViewById(R.id.sitem_img);
            itemTitle = itemView.findViewById(R.id.sitem_title);
        }
    }
}
