package org.meicode.appfilm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.meicode.appfilm.Activity.MovieDetailsActivity;
import org.meicode.appfilm.R;
import org.meicode.appfilm.Models.Movie;

import java.util.List;

public class ItemRcViewAdapter extends RecyclerView.Adapter<ItemRcViewAdapter.ItemViewHolder> {
    Context cont;
    List<Movie> ItemList;

    public ItemRcViewAdapter(Context cont, List<Movie> itemList) {
        this.cont = cont;
        ItemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(cont).inflate(R.layout.cat_rcview_item_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Movie movie = ItemList.get(position);
        holder.itemTitle.setText(movie.getTitle());
        Glide.with(cont).load(movie.getPoster()).into(holder.itemImg);
        holder.itemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(cont, MovieDetailsActivity.class);
                i.putExtra("movieId",movie.getId());
                i.putExtra("movieName",movie.getTitle());
                i.putExtra("movieImageUrl",movie.getPoster());
                i.putExtra("movieFile",movie.getFileUrl());
                i.putExtra("movieDescription",movie.getDescription());
                i.putExtra("movieRate", String.valueOf(movie.getRated()));
                cont.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    public static final class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImg;
        TextView itemTitle;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = (ImageView) itemView.findViewById(R.id.item_img);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
        }
    }
}
