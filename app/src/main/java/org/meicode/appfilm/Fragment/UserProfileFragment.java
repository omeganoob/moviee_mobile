package org.meicode.appfilm.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.meicode.appfilm.Activity.MainActivity;
import org.meicode.appfilm.R;

public class UserProfileFragment extends Fragment {
    private static final String TAG = "User Profile";
    private Activity mActivity;

    private SharedPreferences userSharedPreferences, loginSharedPreferences;

    private Button SignOutBtn;

    private TextView profileName, profileEmail;
    private ImageView profileImage;

    private View mView;

    public UserProfileFragment(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        mView = getView();
        if(mView != null) {
            initViews(mView);
            initUserInfo();
        }

    }

    private void initViews(View view) {
        SignOutBtn = view.findViewById(R.id.SignOutBtn);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileImage = view.findViewById(R.id.profileImage);

        SignOutBtn.setOnClickListener(v -> {
            signOut();
        });
    }

    private void initUserInfo() {
        loginSharedPreferences = mActivity.getApplicationContext().getSharedPreferences("isLoggedIn", mActivity.MODE_PRIVATE);
        userSharedPreferences = mActivity.getApplicationContext().getSharedPreferences("user", mActivity.MODE_PRIVATE);
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

    private void signOut() {
        loginSharedPreferences.edit().putBoolean("isLoggedIn", false).apply();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        return view;
    }
}