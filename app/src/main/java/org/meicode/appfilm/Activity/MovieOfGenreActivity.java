package org.meicode.appfilm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.meicode.appfilm.Adapter.SearchResultListAdapter;
import org.meicode.appfilm.Utils.AppConstraint;
import org.meicode.appfilm.Models.Movie;
import org.meicode.appfilm.API.retrofitresponse.MovieResponse;
import org.meicode.appfilm.API.retrofitservices.MovieService;
import org.meicode.appfilm.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieOfGenreActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    BottomNavigationView bottomNavigationView;
    RecyclerView movieOfGenreList;
    ImageView expander;
    CoordinatorLayout mogParent;
    //    int genreId;
    String genreName;
    String genreIDs;
    String nameandid;
    boolean isExpanded = true;

    SearchResultListAdapter resultListAdapter;
    List<Movie> resultList;

    HashMap<String, Integer> listOfGenre = new HashMap<>();

    ChipGroup genreChips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_of_genre);
        genreIDs = getIntent().getIntExtra("genreID", 0) + "";
        genreName = getIntent().getStringExtra("genreName");
        initViews();

        //Call getResultList on first run
        getResultList(genreIDs);
        getTags();
        //Render tags
        renderChips();

        resultListAdapter = new SearchResultListAdapter(this);
        resultListAdapter.setResultList(resultList);

        movieOfGenreList.setLayoutManager(new GridLayoutManager(this, 2));
        movieOfGenreList.setAdapter(resultListAdapter);
        expander.setOnClickListener(v -> {
            expand();
        });
    }

    private void getTags() {
        /**
         * Get the list of name and id of genres form sharedpreference and put to hash map
         */
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Integer>>() {
        }.getType();
        sharedPreferences = getSharedPreferences("listOfGenre", MODE_PRIVATE);
        String json = sharedPreferences.getString("list", null);
        Log.d("json", json);
        listOfGenre = gson.fromJson(json, type);
        Log.d("listOfGenre", listOfGenre.size() + "");
    }

    private void expand() {
        isExpanded = !isExpanded;
        if (isExpanded) {
            TransitionManager.beginDelayedTransition(mogParent);
            genreChips.setVisibility(View.VISIBLE);
            expander.setImageResource(R.drawable.ic_round_arrow_drop_up);
        }
        if (!isExpanded) {
            TransitionManager.beginDelayedTransition(mogParent);
            genreChips.setVisibility(View.GONE);
            expander.setImageResource(R.drawable.ic_round_arrow_drop_down);
        }
    }

    private void renderChips() {
        LayoutInflater inflater = LayoutInflater.from(this);
        Iterator iterator = listOfGenre.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            Chip chip = (Chip) inflater.inflate(R.layout.chip_item, genreChips, false);
            if (genreIDs.contains(pair.getValue() + "")) {
                chip.setChecked(true);
            }
            chip.setText((String) pair.getKey());
            chip.setOnCloseIconClickListener(v -> {
                genreChips.removeView(v);
            });
            chip.setOnClickListener(v -> {
                String id = String.valueOf(listOfGenre.get(chip.getText()));
                if (genreIDs.contains(id)) {
                    genreIDs = genreIDs.replace("," + id, "");
                    genreIDs = genreIDs.replace(id, "");
                } else {
                    if (genreIDs.length() < 1) {
                        genreIDs += (id);
                    } else {
                        genreIDs += ("," + id);
                    }
                }
                Log.d("ids", genreIDs);
                getResultList(genreIDs);
            });
            genreChips.addView(chip);
        }
    }

    private void getResultList(String ids) {
        resultList = new ArrayList<>();

        AppConstraint.retrofit.create(MovieService.class).getMovieWithGenre(ids)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                resultList.addAll(response.body().getMovies());
                                Log.d("resultList", String.valueOf(resultList.size()));
                                resultListAdapter.setResultList(resultList);
                            } else {
                                resultList.clear();
                                resultListAdapter.setResultList(resultList);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }

    private void initViews() {
        mogParent = findViewById(R.id.mogParent);
        genreChips = findViewById(R.id.genreChips);
        movieOfGenreList = findViewById(R.id.movieOfGenreList);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
        expander = findViewById(R.id.expander);
        setUpBottomNavigation();
    }

    private void setUpBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            bottomNavigationView.getMenu().setGroupCheckable(0, true, true);
            SharedPreferences loginSharedPreferences = getApplicationContext().getSharedPreferences("isLoggedIn", MODE_PRIVATE);
            Boolean isLoggin = loginSharedPreferences.getBoolean("isLoggedIn", false);
            switch (item.getItemId()) {
                case R.id.menu_item_home:
                case R.id.menu_item_fav:
                case R.id.menu_item_acc:
                    Intent main = new Intent(this, MainActivity.class);
                    startActivity(main);
                    return true;
                default:
                    return false;
            }
        });
    }
}