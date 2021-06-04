package org.meicode.appfilm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.meicode.appfilm.adapter.GenreListAdapter;
import org.meicode.appfilm.model.Genre;
import org.meicode.appfilm.retrofitresponse.GenreResponse;
import org.meicode.appfilm.retrofitresponse.MovieResponse;
import org.meicode.appfilm.retrofitservices.MovieService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenreListActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    RecyclerView genreList;

    GenreListAdapter genreListAdapter;
    List<Genre> genres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_list);
        initViews();

        genreListAdapter = new GenreListAdapter(this);
        getGenreList();
        genreListAdapter.setGenres(genres);
        genreList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        genreList.setAdapter(genreListAdapter);

    }

    private void getGenreList() {
        genres = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://192.168.56.1/moviee/public/api/v1/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
        retrofit.create(MovieService.class).getGenres()
                .enqueue(new Callback<GenreResponse>() {
                    @Override
                    public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getGenre().size() > 0) {
                                genres.addAll(response.body().getGenre());
                                Log.d("NewestList", String.valueOf(genres.size()));
                            }
                            genreListAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenreResponse> call, Throwable t) {

                    }
                });
    }

    private void initViews() {
        genreList = findViewById(R.id.genreList);
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