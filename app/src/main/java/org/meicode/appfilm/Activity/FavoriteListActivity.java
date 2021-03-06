package org.meicode.appfilm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.meicode.appfilm.Adapter.UserFavoriteListAdapter;
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

public class FavoriteListActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView UserFavoriteList;
    List<Movie> FavoriteListItems;

    UserFavoriteListAdapter userFavoriteListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        initViews();
        setUpBottomNavigation();
        getUserFavoriteList();

        userFavoriteListAdapter = new UserFavoriteListAdapter(this);
        userFavoriteListAdapter.setFavoriteList(FavoriteListItems);
        UserFavoriteList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        UserFavoriteList.setAdapter(userFavoriteListAdapter);
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        UserFavoriteList = findViewById(R.id.UserFavoriteList);
    }

    private void getUserFavoriteList() {
        FavoriteListItems = new ArrayList<>();
        SharedPreferences userSharedPreferences = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        String id = userSharedPreferences.getString("id", "0");

        AppConstraint.retrofit.create(MovieService.class).getFavorite(Long.parseLong(id))
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                FavoriteListItems.addAll(response.body().getMovies());
                                Log.d("TvShowsBannerList", String.valueOf(FavoriteListItems.size()));
                            }
                            userFavoriteListAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }

    private void setUpBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.menu_item_fav);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_acc:
                    SharedPreferences loginSharedPreferences = getApplicationContext().getSharedPreferences("isLoggedIn", MODE_PRIVATE);
                    Boolean isLoggin = loginSharedPreferences.getBoolean("isLoggedIn", false);
                    if (isLoggin) {
                        // neu da dang nhap
                        Intent userProfileIntent = new Intent(this, UserProfileActivity.class);
                        startActivity(userProfileIntent);
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                    }
                    return true;
                case R.id.menu_item_home:
                    startActivity(new Intent(this, MainActivity.class));
                    return true;
                case R.id.menu_item_fav:
                    Toast.makeText(this, "B???n ??ang ??? ????y n??", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        });
    }
}