package org.meicode.appfilm;

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

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.meicode.appfilm.model.User;
import org.meicode.appfilm.retrofitresponse.UserResponse;
import org.meicode.appfilm.retrofitservices.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText inputUsername, inputUserpassword;
    private TextView toSignUp, loginErrMsg;;
    private Button logInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

        toSignUp.setOnClickListener(v -> {
            Intent signUpIntent = new Intent(this, SignUpActivity.class);
            startActivity(signUpIntent);
        });
    }

    private void initViews() {
        inputUsername = findViewById(R.id.inputUsername);
        inputUserpassword = findViewById(R.id.inputUserpassword);
        logInBtn = findViewById(R.id.logInBtn);
        toSignUp = findViewById(R.id.linkToSignUp);
        logInBtn.setOnClickListener(this);

        loginErrMsg = findViewById(R.id.loginErrMsg);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case  R.id.logInBtn: {
                login();
                break;
            }
        }
    }

    private void login() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đăng nhập");
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://192.168.56.1/moviee/public/api/v1/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
        retrofit.create(UserService.class).login(inputUsername.getText().toString(), inputUserpassword.getText().toString())
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        Log.d("LoginSuccess:", response.message());
                        if(response.isSuccessful() && response.body().getSuccess()) {
                            Log.d("success login message:", response.body().getSuccess().toString());
                            // login success
                            SharedPreferences isLoggin = getApplicationContext().getSharedPreferences("isLoggedIn", MODE_PRIVATE);
                            SharedPreferences.Editor editor = isLoggin.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();
                            User user = response.body().getUser();
                            String t = isLoggin.getBoolean("isLoggedIn", false) ? "true" : "false";
                            Log.d("isLoggin", t);
                            SharedPreferences userSharedPreferences = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = userSharedPreferences.edit();
                            editor1.putString("id", String.valueOf(user.getId()));
                            editor1.putString("name", user.getName());
                            editor1.putString("email", user.getEmail());
                            editor1.apply();
                            progressDialog.dismiss();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        else {
                            progressDialog.dismiss();
                            loginErrMsg.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.d("LoginErr:", t.getMessage());
                    }
                });

    }
}