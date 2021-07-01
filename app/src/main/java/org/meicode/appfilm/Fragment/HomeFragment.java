package org.meicode.appfilm.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import org.meicode.appfilm.Activity.MainActivity;
import org.meicode.appfilm.Adapter.BannerMoviesAdapter;
import org.meicode.appfilm.Adapter.MainRcViewAdapter;
import org.meicode.appfilm.Models.Movie;
import org.meicode.appfilm.Models.MovieCategory;
import org.meicode.appfilm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements MainActivity.DataLoadedListener {

    private static final String TAG = "HomeFragment";

    View mView;

    public static BannerMoviesAdapter AdapterBanner;
    public static MainRcViewAdapter AdapterRcView;
    TabLayout IndicatorTab, categoryTab;
    ViewPager banner;
    List<Movie> HomeBannerList;
    List<Movie> TvShowsBannerList;
    List<Movie> MovieBannerList;
    List<Movie> KidsBannerList;

    RecyclerView MainRcView;
    List<MovieCategory> CategoryList;
    NestedScrollView nestedScroll;
    AppBarLayout appBar;

    List<Movie> TopRatedList, PopularList, NewestList, CartoonList;

    Activity main;

    public HomeFragment(Activity main) {
        this.main = main;
    }

    @Override
    public void onStart() {
        super.onStart();
        mView = getView();
        if (mView != null) {
            Log.d(TAG, "view is inflated");
            initViews(mView);
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

            setAdapterBanner(HomeBannerList);
            CategoryList = new ArrayList<>();
            CategoryList = getCateList();
            setAdapterRcView(CategoryList);
        } else {
            Log.d(TAG, "view is null");
        }
    }

    private List<MovieCategory> getCateList() {
        List<MovieCategory> list = new ArrayList<>();
        list.add(new MovieCategory(1, "PHIM ĐỀ CỬ", TopRatedList));
        list.add(new MovieCategory(2, "PHIM PHỔ BIẾN", PopularList));
        list.add(new MovieCategory(3, "PHIM MỚI CẬP NHẬT", NewestList));
        list.add(new MovieCategory(4, "PHIM HOẠT HÌNH", CartoonList));
        return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    private void initViews(View view) {
        IndicatorTab = view.findViewById(R.id.indicator);
        categoryTab = view.findViewById(R.id.tabLayout);
        nestedScroll = view.findViewById(R.id.nested_Scroll);
        appBar = view.findViewById(R.id.appbar);
        banner = view.findViewById(R.id.banner_viewPager);
        MainRcView = view.findViewById(R.id.main_rcview);
    }

    private void setAdapterRcView(List<MovieCategory> CateList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        MainRcView.setLayoutManager(layoutManager);
        AdapterRcView = new MainRcViewAdapter(this.getContext(), CateList);
        MainRcView.setAdapter(AdapterRcView);
    }

    private void setAdapterBanner(List<Movie> bannerList) {
        AdapterBanner = new BannerMoviesAdapter(this.getContext());
        AdapterBanner.setBannerList(bannerList);
        banner.setAdapter(AdapterBanner);
        IndicatorTab.setupWithViewPager(banner);
        Timer slider = new Timer();
        slider.scheduleAtFixedRate(new HomeFragment.AutoSlider(), 4000, 6000);
        IndicatorTab.setupWithViewPager(banner, true);
    }

    private void setScrollDefaultState() {
        nestedScroll.fullScroll(View.FOCUS_UP);
        nestedScroll.scrollTo(0, 0);
        appBar.setExpanded(true);
    }

    @Override
    public void OnDataLoaded(List<Movie> HomeBannerList,
                             List<Movie> TvShowsBannerList,
                             List<Movie> MovieBannerList,
                             List<Movie> KidsBannerList,
                             List<Movie> TopRatedList,
                             List<Movie> PopularList,
                             List<Movie> NewestList,
                             List<Movie> CartoonList) {

        this.HomeBannerList = HomeBannerList;
        this.TvShowsBannerList = TvShowsBannerList;
        this.MovieBannerList = MovieBannerList;
        this.KidsBannerList = KidsBannerList;

        this.TopRatedList = TopRatedList;
        this.PopularList = PopularList;
        this.NewestList = NewestList;
        this.CartoonList = CartoonList;
    }

    class AutoSlider extends TimerTask {

        @Override
        public void run() {
            main.runOnUiThread(new Runnable() {
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
}