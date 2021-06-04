package org.meicode.appfilm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserProfileActivity extends AppCompatActivity {

    private SharedPreferences userSharedPreferences, loginSharedPreferences;

    private BottomNavigationView bottomNavigationView;
    private Button SignOutBtn;

    private TextView profileName, profileEmail;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initViews();
        initUserInfo();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
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
        
        SignOutBtn.setOnClickListener(v -> {
            signOut();
        });
    }

    private void signOut() {
//        userSharedPreferences.edit().clear().apply();
        loginSharedPreferences.edit().putBoolean("isLoggedIn", false).apply();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void initViews() {
        SignOutBtn = findViewById(R.id.SignOutBtn);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileImage = findViewById(R.id.profileImage);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_item_acc);
    }

    private void initUserInfo() {
        loginSharedPreferences = getApplicationContext().getSharedPreferences("isLoggedIn", MODE_PRIVATE);
        userSharedPreferences = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        String id = userSharedPreferences.getString("id", "default value");
        String userName = userSharedPreferences.getString("name","user name");
        String userEmail = userSharedPreferences.getString("email","user email");
        Glide.with(this)
                .asBitmap()
                .load("https://lh3.googleusercontent.com/proxy/rQg1Fx2w8cNLOiOwo1Bdn3EO-zIJ9TRI7s3m6Rif1zWFJCzXsytDIefW-2QTiw7OPoK5nB57DM1JF-KPeKBeCyxX-jm5FrdPLUUOy3bcvzrohASQbP0UxQog9w=w1200-h630-p-k-no-nu")
                .override(150, 150)
                .fitCenter()
                .into(profileImage);
        profileName.setText(userName);
        profileEmail.setText(userEmail);
    }
}