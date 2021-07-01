package org.meicode.appfilm.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.meicode.appfilm.API.retrofitresponse.MovieResponse;
import org.meicode.appfilm.API.retrofitservices.MovieService;
import org.meicode.appfilm.Adapter.UserFavoriteListAdapter;
import org.meicode.appfilm.Models.Movie;
import org.meicode.appfilm.R;
import org.meicode.appfilm.Utils.AppConstraint;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteListFragment extends Fragment {
    private static final String TAG = "Favorite List Fragment";
    private Activity mActivity;
    public static boolean isLoaded = false;

    BottomNavigationView bottomNavigationView;
    RecyclerView UserFavoriteList;
    List<Movie> FavoriteListItems;

    UserFavoriteListAdapter userFavoriteListAdapter;

    View mView;

    public FavoriteListFragment(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        mView = getView();
        if (mView != null) {
            Log.d(TAG, "layout inflated");
            initViews(mView);
            if (!isLoaded) {
                isLoaded = true;
                this.FavoriteListItems = getUserFavoriteList();
                userFavoriteListAdapter = new UserFavoriteListAdapter(getContext());
                userFavoriteListAdapter.setFavoriteList(FavoriteListItems);
                UserFavoriteList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                UserFavoriteList.setAdapter(userFavoriteListAdapter);
            } else {
                userFavoriteListAdapter = new UserFavoriteListAdapter(getContext());
                userFavoriteListAdapter.setFavoriteList(FavoriteListItems);
                UserFavoriteList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                UserFavoriteList.setAdapter(userFavoriteListAdapter);
            }
        }
    }

    private void initViews(View view) {
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        UserFavoriteList = view.findViewById(R.id.UserFavoriteList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        return view;
    }

    private List<Movie> getUserFavoriteList() {
        List<Movie> list = new ArrayList<>();
        SharedPreferences userSharedPreferences = mActivity.getApplicationContext().getSharedPreferences("user", mActivity.MODE_PRIVATE);
        String id = userSharedPreferences.getString("id", "0");

        AppConstraint.retrofit.create(MovieService.class).getFavorite(Long.parseLong(id))
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMovies().size() > 0) {
                                list.addAll(response.body().getMovies());
                                Log.d("Favorite list", String.valueOf(list.size()));
                            }
                            userFavoriteListAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
        return list;
    }
}