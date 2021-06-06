package org.meicode.appfilm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.meicode.appfilm.Adapter.CommentSectionAdapter;
import org.meicode.appfilm.Utils.AppConstraint;
import org.meicode.appfilm.Models.Comment;
import org.meicode.appfilm.API.retrofitresponse.AddFavResponse;
import org.meicode.appfilm.API.retrofitresponse.CheckFavoriteResponse;
import org.meicode.appfilm.API.retrofitresponse.CommentResponse;
import org.meicode.appfilm.API.retrofitservices.MovieService;
import org.meicode.appfilm.API.retrofitservices.UserService;
import org.meicode.appfilm.R;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    Integer user_id;

    ImageView movie_img, movie_icon;
    Button play_btn;
    TextView movie_name, movie_des, movie_rate;
    Integer mId;
    String mName,mImage,mFileUrl, mDescription, mRate;

    EditText commentInput;
    ImageButton btnComment;
    RecyclerView commentSectionList;
    CommentSectionAdapter commentSectionAdapter;
    List<Comment> commentList;

    ImageButton favoriteBtn;
    Boolean isLiked = false, isLoggin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        SharedPreferences loginSharedPreferences = getApplicationContext().getSharedPreferences("isLoggedIn", MODE_PRIVATE);
        isLoggin = loginSharedPreferences.getBoolean("isLoggedIn", false);
        SharedPreferences userSharedPreferences = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        user_id = Integer.parseInt(userSharedPreferences.getString("id", "0"));
        initViews();
        setInfo();
        Log.d("Movie Id", String.valueOf(mId));
        checkIfFav(mId);
        favoriteBtn.setOnClickListener(v -> {
            addFavorite(mId);
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            bottomNavigationView.getMenu().setGroupCheckable(0, true, true);
            switch (item.getItemId()) {
                case R.id.menu_item_acc:
                    if(isLoggin) {
                        Intent userProfileIntent = new Intent(this, UserProfileActivity.class);
                        startActivity(userProfileIntent);
                    } else {
                        startActivity(new Intent(MovieDetailsActivity.this, LoginActivity.class));
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
                        startActivity(new Intent(MovieDetailsActivity.this, LoginActivity.class));
                    }
                    return true;
                default:
                    return false;
            }
        });
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MovieDetailsActivity.this,VideoPlayerActivity.class);
                i.putExtra("url",mFileUrl);
                startActivity(i);
            }
        });

        getComments();
        commentSectionAdapter = new CommentSectionAdapter(this);
        commentSectionAdapter.setComments(commentList);
        commentSectionList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        commentSectionList.setAdapter(commentSectionAdapter);

        btnComment.setOnClickListener(v -> {
            comment();
        });
    }

    private void comment() {
        String body = commentInput.getText().toString();
        Log.d("comment body", body);
        commentInput.setText("");
        if (!isLoggin) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            return;
        }

        Log.d("retrofit","build ok");
        AppConstraint.retrofit.create(MovieService.class).comment(mId, user_id, body)
                .enqueue(new Callback<CommentResponse>() {
                    @Override
                    public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getComments().size() > 0) {
                                commentList.addAll(response.body().getComments());
                                Log.d("commentList", String.valueOf(commentList.size()));
                                commentSectionAdapter.notifyDataSetChanged();
                            } else {
                                Log.d("commendList","There is no comment");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<CommentResponse> call, Throwable t) {
                        Log.d("error when comment", t.getMessage());
                    }
                });
    }

    private void getComments() {
        commentList = new ArrayList<>();

        AppConstraint.retrofit.create(MovieService.class).getComments(mId)
                .enqueue(new Callback<CommentResponse>() {
                    @Override
                    public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getComments().size() > 0) {
                                commentList.addAll(response.body().getComments());
                                Log.d("commentList", String.valueOf(commentList.size()));
                                commentSectionAdapter.notifyDataSetChanged();
                            } else {
                                Log.d("commendList","There is no comment");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<CommentResponse> call, Throwable t) {

                    }
                });
    }

    private void addFavorite(Integer movie_id) {

        AppConstraint.retrofit.create(UserService.class).addtofav(user_id, movie_id)
                .enqueue(new Callback<AddFavResponse>() {
                    @Override
                    public void onResponse(Call<AddFavResponse> call, Response<AddFavResponse> response) {
                        Log.d("addtofav message",response.body().getMessage());
                        isLiked = !isLiked;
                        setFavIcon();
                    }
                    @Override
                    public void onFailure(Call<AddFavResponse> call, Throwable t) {

                    }
                });
    }

    private void setFavIcon() {
        if (isLiked) {
            favoriteBtn.setImageResource(R.drawable.like);
        } else {
            favoriteBtn.setImageResource(R.drawable.heart_outline);
        }
    }

    private void setInfo() {
        mId = getIntent().getIntExtra("movieId", 0);
        mName = getIntent().getStringExtra("movieName");
        mImage = getIntent().getStringExtra("movieImageUrl");
        mFileUrl = getIntent().getStringExtra("movieFile");
        mDescription = getIntent().getStringExtra("movieDescription");
        mRate = getIntent().getStringExtra("movieRate");
        Glide.with(this).load(mImage)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                .into(movie_img);
        Glide.with(this)
                .asBitmap()
                .load(mImage)
                .centerCrop()
                .into(movie_icon);
        movie_name.setText(mName);
        movie_des.setText((mDescription));
        movie_rate.setText(mRate);
    }

    private void initViews() {
        favoriteBtn = findViewById(R.id.favoriteBtn);
        setFavIcon();
        movie_img  = findViewById(R.id.movie_img);
        movie_icon = findViewById(R.id.movie_icon);
        movie_name = findViewById(R.id.movie_name);
        movie_rate = findViewById(R.id.movie_rate);
        play_btn= findViewById(R.id.play_btn);
        movie_des = findViewById(R.id.movie_des);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true);

        commentInput = findViewById(R.id.commentInput);
        btnComment = findViewById(R.id.btnComment);
        commentSectionList = findViewById(R.id.commentSectionList);
    }

    public void checkIfFav(Integer movie_id) {

        AppConstraint.retrofit.create(UserService.class).isfav(user_id, movie_id)
                .enqueue(new Callback<CheckFavoriteResponse>() {
                    @Override
                    public void onResponse(Call<CheckFavoriteResponse> call, Response<CheckFavoriteResponse> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getIsfav()) {
                                isLiked = true;
                                Log.d("is Favorite", isLiked.toString());
                                setFavIcon();
                            } else {
                                isLiked = false;
                                Log.d("is Favorite", isLiked.toString());
                                setFavIcon();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<CheckFavoriteResponse> call, Throwable t) {
                    }
                });
    }
}