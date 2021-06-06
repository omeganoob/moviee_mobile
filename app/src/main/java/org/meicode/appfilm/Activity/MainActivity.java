package org.meicode.appfilm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;

    BannerMoviesAdapter AdapterBanner;
    TabLayout IndicatorTab, categoryTab;
    ViewPager banner;
    List<Movie> HomeBannerList;
    List<Movie> TvShowsBannerList;
    List<Movie> MovieBannerList;
    List<Movie> KidsBannerList;

    MainRcViewAdapter AdapterRcView;
    RecyclerView MainRcView;
    List<MovieCategory> CategoryList;
    NestedScrollView nestedScroll;
    AppBarLayout appBar;

    List<Movie> TopRatedList, PopularList, NewestList, CartoonList;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setUpNavigationDrawer();
        setUpBottomNavigation();
        setUpBannerList();
        setUpCatMovieList();

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });

        categoryTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 1:
                        setScrollDefaultState();
                        setAdapterBanner(TvShowsBannerList);
                        return;
                    case 2:
                        setScrollDefaultState();
                        setAdapterBanner(MovieBannerList);
                        return;
                    case 3:
                        setScrollDefaultState();
                        setAdapterBanner(KidsBannerList);
                        return;
                    default:
                        setScrollDefaultState();
                        setAdapterBanner(HomeBannerList);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        CategoryList = getCateList();
        setAdapterRcView(CategoryList);
    }

    private void setUpBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            SharedPreferences loginSharedPreferences = getApplicationContext().getSharedPreferences("isLoggedIn", MODE_PRIVATE);
            Boolean isLoggin = loginSharedPreferences.getBoolean("isLoggedIn", false);
            switch (item.getItemId()) {
                case R.id.menu_item_acc:
                    if (isLoggin) {
                        // neu da dang nhap
                        Intent userProfileIntent = new Intent(this, UserProfileActivity.class);
                        startActivity(userProfileIntent);
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                    return true;
                case R.id.menu_item_home:
                    Toast.makeText(this, "Bạn đang ở đây rồi", Toast.LENGTH_SHORT).show();
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
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                NewestList.addAll(response.body().getMovies());
                                Log.d("NewestList", String.valueOf(NewestList.size()));
                            }
                            AdapterBanner.notifyDataSetChanged();
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
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                PopularList.addAll(response.body().getMovies());
                                Log.d("PopularList", String.valueOf(PopularList.size()));
                            }
                            AdapterBanner.notifyDataSetChanged();
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
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                CartoonList.addAll(response.body().getMovies());
                                Log.d("HomeBannerList", String.valueOf(CartoonList.size()));
                            }
                            AdapterRcView.notifyDataSetChanged();
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
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                TopRatedList.addAll(response.body().getMovies());
                                Log.d("TopRatedList", String.valueOf(TopRatedList.size()));
                            }
                            AdapterBanner.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }

                });
    }

    private List<MovieCategory> getCateList() {
        List<MovieCategory> list = new ArrayList<>();
        list.add(new MovieCategory(1, "PHIM ĐỀ CỬ", TopRatedList));
        list.add(new MovieCategory(2, "PHIM PHỔ BIẾN", PopularList));
        list.add(new MovieCategory(3, "PHIM MỚI CẬP NHẬT", NewestList));
        list.add(new MovieCategory(4, "PHIM HOẠT HÌNH", CartoonList));
        return list;
    }

    private void setUpBannerList() {
        //Home
        setHomeBannerList();
        setTvShowsBannerList();
        setMovieBannerList();
        setKidsBannerList();
        setAdapterBanner(HomeBannerList);
    }

    private void setKidsBannerList() {
        KidsBannerList = new ArrayList<>();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.56.1/moviee/public/api/v1/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
        AppConstraint.retrofit.create(MovieService.class).getMovieByAge(9)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                KidsBannerList.addAll(response.body().getMovies());
                                Log.d("KidsBannerList", String.valueOf(KidsBannerList.size()));
                            }
                            AdapterBanner.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }

                });
    }

    private void setMovieBannerList() {
        MovieBannerList = new ArrayList<>();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.56.1/moviee/public/api/v1/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
        AppConstraint.retrofit.create(MovieService.class).getOnlyMovie()
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                MovieBannerList.addAll(response.body().getMovies());
                                Log.d("MovieBannerList", String.valueOf(MovieBannerList.size()));
                            }
                            AdapterBanner.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }

    private void setTvShowsBannerList() {
        TvShowsBannerList = new ArrayList<>();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.56.1/moviee/public/api/v1/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
        AppConstraint.retrofit.create(MovieService.class).getTvShows()
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                TvShowsBannerList.addAll(response.body().getMovies());
                                Log.d("TvShowsBannerList", String.valueOf(TvShowsBannerList.size()));
                            }
                            AdapterBanner.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });

    }

    private void setHomeBannerList() {
        HomeBannerList = new ArrayList<>();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.56.1/moviee/public/api/v1/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
        AppConstraint.retrofit.create(MovieService.class).getMovies()
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                HomeBannerList.addAll(response.body().getMovies());
                                Log.d("HomeBannerList", String.valueOf(HomeBannerList.size()));
                            }
                            AdapterBanner.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }

    private void initViews() {
        fab = findViewById(R.id.toSearchMovie);
        IndicatorTab = findViewById(R.id.indicator);
        categoryTab = findViewById(R.id.tabLayout);
        nestedScroll = findViewById(R.id.nested_Scroll);
        appBar = findViewById(R.id.appbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setAdapterBanner(List<Movie> bannerList) {
        banner = findViewById(R.id.banner_viewPager);
        AdapterBanner = new BannerMoviesAdapter(this);
        AdapterBanner.setBannerList(bannerList);
        banner.setAdapter(AdapterBanner);
        IndicatorTab.setupWithViewPager(banner);
        Timer slider = new Timer();
        slider.scheduleAtFixedRate(new AutoSlider(), 4000, 6000);
        IndicatorTab.setupWithViewPager(banner, true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        SharedPreferences loginSharedPreferences = getApplicationContext().getSharedPreferences("isLoggedIn", MODE_PRIVATE);
        Boolean isLoggin = loginSharedPreferences.getBoolean("isLoggedIn", false);
        switch (item.getItemId()) {
            case R.id.menu_item_home:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.menu_item_fav:
                if (isLoggin) {
                    // neu da dang nhap
                    Intent userProfileIntent = new Intent(this, FavoriteListActivity.class);
                    startActivity(userProfileIntent);
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                break;
            case R.id.menu_item_acc:
                if (isLoggin) {
                    // neu da dang nhap
                    Intent userProfileIntent = new Intent(this, UserProfileActivity.class);
                    startActivity(userProfileIntent);
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                break;
            case R.id.menu_item_genre:
                Intent intent = new Intent(this, GenreListActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    class AutoSlider extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (banner.getCurrentItem() < HomeBannerList.size() - 1) {
                        banner.setCurrentItem(banner.getCurrentItem() + 1);
                    } else {
                        banner.setCurrentItem(0);
                    }
                }
            });
        }
    }

    public void setAdapterRcView(List<MovieCategory> CateList) {
        MainRcView = findViewById(R.id.main_rcview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        MainRcView.setLayoutManager(layoutManager);
        AdapterRcView = new MainRcViewAdapter(this, CateList);
        MainRcView.setAdapter(AdapterRcView);
    }

    private void setScrollDefaultState() {
        nestedScroll.fullScroll(View.FOCUS_UP);
        nestedScroll.scrollTo(0, 0);
        appBar.setExpanded(true);
    }
}