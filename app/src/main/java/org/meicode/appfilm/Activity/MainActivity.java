package org.meicode.appfilm.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.meicode.appfilm.Adapter.BannerMoviesAdapter;
import org.meicode.appfilm.Adapter.MainRcViewAdapter;
import org.meicode.appfilm.Fragment.FavoriteListFragment;
import org.meicode.appfilm.Fragment.HomeFragment;
import org.meicode.appfilm.Fragment.UserProfileFragment;
import org.meicode.appfilm.Utils.AppConstraint;
import org.meicode.appfilm.Models.MovieCategory;
import org.meicode.appfilm.Models.Movie;
import org.meicode.appfilm.API.retrofitresponse.MovieResponse;
import org.meicode.appfilm.API.retrofitservices.MovieService;
import org.meicode.appfilm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;

    List<Movie> HomeBannerList;
    List<Movie> TvShowsBannerList;
    List<Movie> MovieBannerList;
    List<Movie> KidsBannerList;

    List<Movie> TopRatedList, PopularList, NewestList, CartoonList;
    HomeFragment homeFragment;
    FavoriteListFragment favFragment;
    UserProfileFragment profileFragment;
    DataLoadedListener homeFragmentListener;
    private AppBarConfiguration mAppBarConfiguration;
    FragmentManager fragmentManager;
    private FragmentContainerView fragment;
    private boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment(this);
        homeFragmentListener = homeFragment;

        favFragment = new FavoriteListFragment(this);
        profileFragment = new UserProfileFragment(this);

        initViews();
        setUpNavigationDrawer();
        setUpBottomNavigation();
        if (savedInstanceState == null && !isLoaded) {
            setUpBannerList();
            setUpCatMovieList();
            isLoaded = true;
            homeFragment.OnDataLoaded(
                    HomeBannerList,
                    TvShowsBannerList,
                    MovieBannerList,
                    KidsBannerList,
                    TopRatedList,
                    PopularList,
                    NewestList,
                    CartoonList
            );
        }

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!homeFragment.isAdded()) {
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment, homeFragment, null)
                    .addToBackStack(null)
                    .commit();
        }
        Log.d("Main activity", "restored without load");
    }

    @Override
    protected void onPause() {
        super.onPause();
        isLoaded = true;
        FavoriteListFragment.isLoaded = false;
    }

    public interface DataLoadedListener {
        public void OnDataLoaded(
                List<Movie> HomeBannerList,
                List<Movie> TvShowsBannerList,
                List<Movie> MovieBannerList,
                List<Movie> KidsBannerList,
                List<Movie> TopRatedList,
                List<Movie> PopularList,
                List<Movie> NewestList,
                List<Movie> CartoonList
        );
    }

    private void setUpBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            SharedPreferences loginSharedPreferences = getApplicationContext().getSharedPreferences("isLoggedIn", MODE_PRIVATE);
            Boolean isLoggin = loginSharedPreferences.getBoolean("isLoggedIn", false);
            switch (item.getItemId()) {
                case R.id.menu_item_acc:
                    if (isLoggin) {
                        // neu da dang nhap
                        fragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.fragment, profileFragment, null)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                    return true;
                case R.id.menu_item_home:
                    fragmentManager.beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment, homeFragment, null)
                            .addToBackStack(null)
                            .commit();
                    return true;
                case R.id.menu_item_fav:
                    if (isLoggin) {
                        // neu da dang nhap
                        fragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.fragment, favFragment, null)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                    return true;
                default:
                    return false;
            }
        });
    }

    private void setUpNavigationDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.menu_item_home, R.id.menu_item_fav, R.id.menu_item_acc, R.id.menu_item_genre)
                .setOpenableLayout(drawer)
                .build();
    }

    private void setUpCatMovieList() {
        //ItemHome1
        setTopRatedList();
        //ItemHome2
        setPopularList();
        //ItemHome3
        setNewestList();
        //ItemHome4
        setCartoonList();
    }

    private void setNewestList() {
        NewestList = new ArrayList<>();
        AppConstraint.retrofit.create(MovieService.class).getNewest()
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                NewestList.addAll(response.body().getMovies());
                                Log.d("NewestList", String.valueOf(NewestList.size()));
                            }
                            HomeFragment.AdapterRcView.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                    }
                });
    }

    private void setPopularList() {
        PopularList = new ArrayList<>();

        AppConstraint.retrofit.create(MovieService.class).getPopular()
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                PopularList.addAll(response.body().getMovies());
                                Log.d("PopularList", String.valueOf(PopularList.size()));
                            }
                            HomeFragment.AdapterRcView.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }

                });
    }

    private void setCartoonList() {
        CartoonList = new ArrayList<>();

        AppConstraint.retrofit.create(MovieService.class).getMovieWithGenre(4)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                CartoonList.addAll(response.body().getMovies());
                                Log.d("HomeBannerList", String.valueOf(CartoonList.size()));
                            }
                            HomeFragment.AdapterRcView.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }

    private void setTopRatedList() {
        TopRatedList = new ArrayList<>();

        AppConstraint.retrofit.create(MovieService.class).getTopRated()
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                TopRatedList.addAll(response.body().getMovies());
                                Log.d("TopRatedList", String.valueOf(TopRatedList.size()));
                            }
                            HomeFragment.AdapterRcView.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }

                });
    }

    private void setUpBannerList() {
        setHomeBannerList();
        setTvShowsBannerList();
        setMovieBannerList();
        setKidsBannerList();
    }

    private void setKidsBannerList() {
        KidsBannerList = new ArrayList<>();
        AppConstraint.retrofit.create(MovieService.class).getMovieByAge(9)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                KidsBannerList.addAll(response.body().getMovies());
                                Log.d("KidsBannerList", String.valueOf(KidsBannerList.size()));
                            }
                            HomeFragment.AdapterBanner.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }

                });
    }

    private void setMovieBannerList() {
        MovieBannerList = new ArrayList<>();
        AppConstraint.retrofit.create(MovieService.class).getOnlyMovie()
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                MovieBannerList.addAll(response.body().getMovies());
                                Log.d("MovieBannerList", String.valueOf(MovieBannerList.size()));
                            }
                            HomeFragment.AdapterBanner.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }

    private void setTvShowsBannerList() {
        TvShowsBannerList = new ArrayList<>();
        AppConstraint.retrofit.create(MovieService.class).getTvShows()
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                TvShowsBannerList.addAll(response.body().getMovies());
                                Log.d("TvShowsBannerList", String.valueOf(TvShowsBannerList.size()));
                            }
                            HomeFragment.AdapterBanner.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });

    }

    private void setHomeBannerList() {
        HomeBannerList = new ArrayList<>();
        AppConstraint.retrofit.create(MovieService.class).getMovies()
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                HomeBannerList.addAll(response.body().getMovies());
                                Log.d("HomeBannerList", String.valueOf(HomeBannerList.size()));
                            }
                            HomeFragment.AdapterBanner.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }

    private void initViews() {
        fragment = findViewById(R.id.fragment);
        fab = findViewById(R.id.toSearchMovie);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        SharedPreferences loginSharedPreferences = getApplicationContext().getSharedPreferences("isLoggedIn", MODE_PRIVATE);
        Boolean isLoggin = loginSharedPreferences.getBoolean("isLoggedIn", false);
        switch (item.getItemId()) {
            case R.id.menu_item_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.menu_item_fav:
                if (isLoggin) {
                    // neu da dang nhap
                    Intent userProfileIntent = new Intent(this, FavoriteListActivity.class);
                    startActivity(userProfileIntent);
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                return true;
            case R.id.menu_item_acc:
                if (isLoggin) {
                    // neu da dang nhap
                    Intent userProfileIntent = new Intent(this, UserProfileActivity.class);
                    startActivity(userProfileIntent);
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                return true;
            case R.id.menu_item_genre:
                Intent intent = new Intent(this, GenreListActivity.class);
                startActivity(intent);
                return true;
        }
        return true;
    }
}