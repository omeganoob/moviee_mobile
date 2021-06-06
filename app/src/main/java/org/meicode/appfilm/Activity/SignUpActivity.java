package org.meicode.appfilm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.meicode.appfilm.Utils.AppConstraint;
import org.meicode.appfilm.Models.User;
import org.meicode.appfilm.API.retrofitresponse.UserResponse;
import org.meicode.appfilm.API.retrofitservices.UserService;
import org.meicode.appfilm.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText signUpUsername, SignUpUserPassword, SignUpEmail;
    private Button SignUpBtn;
    private TextView linkToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();

        linkToLogin.setOnClickListener(v -> {
            Intent toLogInIntent = new Intent(this, LoginActivity.class);
            startActivity(toLogInIntent);
        });
    }

    private void initViews() {
        signUpUsername = findViewById(R.id.signUpUsername);
        SignUpEmail = findViewById(R.id.SignUpEmail);
        SignUpUserPassword = findViewById(R.id.SignUpUserPassword);
        SignUpBtn = findViewById(R.id.SignUpBtn);
        linkToLogin = findViewById(R.id.linkToLogin);

        SignUpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SignUpBtn:
                register();
        }
    }

    private void register() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đăng ký");

        AppConstraint.retrofit.create(UserService.class).register(signUpUsername.getText().toString(), SignUpEmail.getText().toString(), SignUpUserPassword.getText().toString())
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if(response.isSuccessful() && response.body().getSuccess()) {
                            // login success
                            SharedPreferences isLoggin = getApplicationContext().getSharedPreferences("isLoggedIn", MODE_PRIVATE);
                            SharedPreferences.Editor editor = isLoggin.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();
                            User user = response.body().getUser();
                            SharedPreferences userSharedPreferences = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = userSharedPreferences.edit();
                            editor1.putString("id", String.valueOf(user.getId()));
                            editor1.putString("name", user.getName());
                            editor1.putString("email", user.getEmail());
                            editor1.apply();
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this,"Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, UserProfileActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Log.d("LoginErr:", t.getMessage());
                    }
                });
    }
}