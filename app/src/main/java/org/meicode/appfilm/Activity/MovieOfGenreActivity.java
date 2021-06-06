package org.meicode.appfilm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.meicode.appfilm.Adapter.SearchResultListAdapter;
import org.meicode.appfilm.Utils.AppConstraint;
import org.meicode.appfilm.Models.Movie;
import org.meicode.appfilm.API.retrofitresponse.MovieResponse;
import org.meicode.appfilm.API.retrofitservices.MovieService;
import org.meicode.appfilm.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieOfGenreActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    RecyclerView movieOfGenreList;
    int genreId;
    String genreName;

    TextView txtGenreName;

    SearchResultListAdapter resultListAdapter;
    List<Movie> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_of_genre);
        genreId = getIntent().getIntExtra("genreID", 0);
        genreName = getIntent().getStringExtra("genreName");
        initViews();
        getResultList();
        resultListAdapter = new SearchResultListAdapter(this);
        resultListAdapter.setResultList(resultList);

        movieOfGenreList.setLayoutManager(new GridLayoutManager(this, 2));
        movieOfGenreList.setAdapter(resultListAdapter);
    }

    private void getResultList() {
        resultList = new ArrayList<>();

        AppConstraint.retrofit.create(MovieService.class).getMovieWithGenre(genreId)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                resultList.addAll(response.body().getMovies());
                                Log.d("resultList", String.valueOf(resultList.size()));
                            }
                            resultListAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }

    private void initViews() {
        movieOfGenreList = findViewById(R.id.movieOfGenreList);
        txtGenreName = findViewById(R.id.genreName);
        String title = "Phim ".concat(genreName);
        txtGenreName.setText(title);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
        setUpBottomNavigation();
    }
    private void setUpBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            bottomNavigationView.getMenu().setGroupCheckable(0, true, true);
            SharedPreferences loginSharedPreferences = getApplicationContext().getSharedPreferences("isLoggedIn", MODE_PRIVATE);
            Boolean isLoggin = loginSharedPreferences.getBoolean("isLoggedIn", false);
            switch (item.getItemId()) {
                case R.id.menu_item_acc:
                    if (isLoggin) {
                        // neu da dang nhap
                        Intent userProfileIntent = new Intent(this, UserProfileActivity.class);
                        startActivity(userProfileIntent);
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                    }
                    return true;
                case R.id.menu_item_home:
                    Intent main = new Intent(this, MainActivity.class);
                    startActivity(main);
                    return true;
                case R.id.menu_item_fav:
                    if (isLoggin) {
                        // neu da dang nhap
                        Intent userProfileIntent = new Intent(this, FavoriteListActivity.class);
                        startActivity(userProfileIntent);
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                    }
                    return true;
                default:
                    return false;
            }
        });
    }
}