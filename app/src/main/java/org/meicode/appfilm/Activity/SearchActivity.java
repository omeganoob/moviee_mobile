package org.meicode.appfilm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class SearchActivity extends AppCompatActivity {

    EditText searchQuery;
    ImageButton btnSearch;
    TextView emptySearch;
    BottomNavigationView bottomNavigationView;
    RecyclerView searchResultList;
    SearchResultListAdapter searchResultListAdapter;
    List<Movie> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        resultList = new ArrayList<>();
        searchResultListAdapter = new SearchResultListAdapter(this);
        searchResultListAdapter.setResultList(resultList);
        btnSearch.setOnClickListener(v -> {
            searchQuery.clearFocus();
            Toast.makeText(this, "Searching", Toast.LENGTH_SHORT).show();
            getResultList();
            Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
        });

        searchResultList.setLayoutManager(new GridLayoutManager(this, 2));
        searchResultList.setAdapter(searchResultListAdapter);
    }

    private void getResultList() {
        resultList.clear();
        searchResultListAdapter.notifyDataSetChanged();
        String query = searchQuery.getText().toString();
        Log.d("search query", query);

        AppConstraint.retrofit.create(MovieService.class).search(query)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                resultList.addAll(response.body().getMovies());
                                Log.d("resultList", String.valueOf(resultList.size()));
                            }
                            searchResultListAdapter.notifyDataSetChanged();
                            if(resultList.size() > 0) {
                                emptySearch.setVisibility(View.GONE);
                            } else {
                                emptySearch.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }

    private void initViews() {
        emptySearch = findViewById(R.id.empty_search);
        searchQuery = findViewById(R.id.searchQuery);
        btnSearch = findViewById(R.id.btnSearch);
        searchResultList = findViewById(R.id.searchResultList);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            bottomNavigationView.getMenu().setGroupCheckable(0, true, true);
            SharedPreferences loginSharedPreferences = getApplicationContext().getSharedPreferences("isLoggedIn", MODE_PRIVATE);
            Boolean isLoggin = loginSharedPreferences.getBoolean("isLoggedIn", false);
            switch (item.getItemId()) {
                case R.id.menu_item_acc:
                    if(isLoggin) {
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
                    if(isLoggin) {
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