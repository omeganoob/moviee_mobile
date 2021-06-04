package org.meicode.appfilm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import org.meicode.appfilm.MovieDetailsActivity;
import org.meicode.appfilm.R;
import org.meicode.appfilm.model.BannerMovie;
import org.meicode.appfilm.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class BannerMoviesAdapter extends PagerAdapter {
    Context cont;
    List<Movie> BannerList;

    public BannerMoviesAdapter(Context cont) {
        this.cont = cont;
    }

    @Override
    public int getCount() {
        return BannerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setBannerList(List<Movie> list) {
        BannerList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(cont).inflate(R.layout.banner_movies,null);
        ImageView imgBanner = view.findViewById(R.id.banner_img);
        Glide.with(cont)
                .load(BannerList.get(position).getPoster())
                .centerCrop()
                .into(imgBanner);
        container.addView(view);
        imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie = BannerList.get(position);
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
        return view;
    }
}
