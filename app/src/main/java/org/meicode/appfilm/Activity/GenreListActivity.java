package org.meicode.appfilm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.meicode.appfilm.Adapter.GenreListAdapter;
import org.meicode.appfilm.Utils.AppConstraint;
import org.meicode.appfilm.Models.Genre;
import org.meicode.appfilm.API.retrofitresponse.GenreResponse;
import org.meicode.appfilm.API.retrofitservices.MovieService;
import org.meicode.appfilm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreListActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    RecyclerView genreList;
    SharedPreferences sharedPreferences;
    GenreListAdapter genreListAdapter;
    List<Genre> genres;
    public static HashMap<String, Integer> nameAndIDs = new HashMap<String, Integer>();

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
        AppConstraint.retrofit.create(MovieService.class).getGenres()
                .enqueue(new Callback<GenreResponse>() {
                    @Override
                    public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getGenre().size() > 0) {
                                genres.addAll(response.body().getGenre());
                                Log.d("NewestList", String.valueOf(genres.size()));
                                for (Genre g: genres) {
                                    nameAndIDs.put(g.getName(), g.getId());
                                }
                                /**
                                 * put name adn id of genre to sharedpreference
                                 */
                                Log.d("nameAndIDs", nameAndIDs.size()+"");
                                sharedPreferences = getSharedPreferences("listOfGenre", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("list", new Gson().toJson(nameAndIDs));
                                editor.apply();
                                Log.d("Save istOfGenre", sharedPreferences.getString("list", "empty"));

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