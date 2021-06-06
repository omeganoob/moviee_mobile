package org.meicode.appfilm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;
import org.meicode.appfilm.Activity.MovieOfGenreActivity;
import org.meicode.appfilm.R;
import org.meicode.appfilm.Models.Genre;

import java.util.List;

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.Holder> {

    private Context context;
    private List<Genre> genres;

    public GenreListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public GenreListAdapter.Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_list_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GenreListAdapter.Holder holder, int position) {
        Genre genre = genres.get(position);
        holder.genreName.setText(genre.getName());

        holder.genreItem.setOnClickListener(v -> {
            int genreId = genre.getId();
            String genreName = genre.getName();
            Intent intent = new Intent(context, MovieOfGenreActivity.class)
                    .putExtra("genreID", genreId)
                    .putExtra("genreName", genreName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public void setGenres(List<Genre> list) {
        genres = list;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private TextView genreName;
        private MaterialCardView genreItem;
        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews() {
            genreItem = itemView.findViewById(R.id.genre_item);
            genreName = itemView.findViewById(R.id.txtGenreName);
        }
    }
}
