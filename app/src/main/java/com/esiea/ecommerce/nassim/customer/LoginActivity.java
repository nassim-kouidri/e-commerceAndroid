package com.esiea.ecommerce.nassim.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.esiea.ecommerce.nassim.R;
import com.esiea.ecommerce.nassim.admin.ProductAdminActivity;
import com.esiea.ecommerce.nassim.admin.UserAdminActivity;
import com.esiea.ecommerce.nassim.model.AuthResponse;
import com.esiea.ecommerce.nassim.model.LoginRequest;
import com.esiea.ecommerce.nassim.model.User;
import com.esiea.ecommerce.nassim.network.NetworkManager;
import com.esiea.ecommerce.nassim.network.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(view -> loginUser());

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        RetrofitService retrofitService = NetworkManager.getRetrofitService();
        Call<AuthResponse> call = retrofitService.loginUser(loginRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

                    saveTokens(authResponse.getAccessToken(), authResponse.getRefreshToken());

                    getUserProfile();
                } else {
                    Toast.makeText(LoginActivity.this, "Le mot de passe ou l'email est incorrect", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                // Erreur réseau
                Toast.makeText(LoginActivity.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveTokens(String accessToken, String refreshToken) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("access_token", accessToken);
        editor.putString("refresh_token", refreshToken);
        editor.apply();
    }

    private void getUserProfile() {
        RetrofitService retrofitService = NetworkManager.getRetrofitService();
        String accessToken = getAccessToken();

        if (accessToken != null) {
            Call<User> call = retrofitService.getUserProfile("Bearer " + accessToken);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body();

                        if ("admin".equals(user.getRole())) {
                            Intent intent = new Intent(LoginActivity.this, ProductAdminActivity.class);
                            startActivity(intent);
                        } else if ("customer".equals(user.getRole())) {
                            Intent intent = new Intent(LoginActivity.this, ProductActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Erreur lors de la récupération du profil utilisateur", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    Toast.makeText(LoginActivity.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String getAccessToken() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("access_token", null);
    }
}
