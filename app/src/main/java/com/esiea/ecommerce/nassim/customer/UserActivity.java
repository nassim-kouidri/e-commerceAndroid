package com.esiea.ecommerce.nassim.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.esiea.ecommerce.nassim.R;
import com.esiea.ecommerce.nassim.model.User;
import com.esiea.ecommerce.nassim.model.UserUpdateRequest;
import com.esiea.ecommerce.nassim.network.NetworkManager;
import com.esiea.ecommerce.nassim.network.RetrofitService;
import com.esiea.ecommerce.nassim.utils.BottomNavigationHelper;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private ImageView ivUserAvatar;
    private EditText etUserName, etUserEmail, etUserPassword;
    private TextView tvUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ivUserAvatar = findViewById(R.id.ivUserAvatar);
        etUserName = findViewById(R.id.etUserName);
        etUserEmail = findViewById(R.id.etUserEmail);
        etUserPassword = findViewById(R.id.etUserPassword);
        tvUserId = findViewById(R.id.tvUserId);
        Button btnModify = findViewById(R.id.btnModify);
        Button btnLogout = findViewById(R.id.btnLogout);

        fetchUserProfile();

        // Bouton de modification
        btnModify.setOnClickListener(v -> {
            String newName = etUserName.getText().toString().trim();
            String newEmail = etUserEmail.getText().toString().trim();
            String newPassword = etUserPassword.getText().toString().trim();

            int userId = Integer.parseInt(tvUserId.getText().toString());

            if (!newName.isEmpty() && !newEmail.isEmpty() && !newPassword.isEmpty()) {
                modifyUserProfile(userId, newName, newEmail, newPassword);
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
        });

        // Bouton de déconnexion
        btnLogout.setOnClickListener(v -> logout());

        BottomNavigationHelper.setupBottomNavigation(this, R.id.navigation_product);
    }


    private void fetchUserProfile() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken = preferences.getString("access_token", "");
        String refreshToken = preferences.getString("refresh_token", "");

        if (!accessToken.isEmpty() && !refreshToken.isEmpty()) {
            RetrofitService retrofitService = NetworkManager.getRetrofitInstance().create(RetrofitService.class);
            Call<User> call = retrofitService.getUserProfile("Bearer " + accessToken);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body();
                        updateUI(user);
                    } else {
                        Toast.makeText(UserActivity.this, "Erreur de chargement des informations de l'utilisateur", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    // Erreur réseau
                    Toast.makeText(UserActivity.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {
            Toast.makeText(UserActivity.this, "Les informations d'authentification sont absentes", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(User user) {
        String imageUrl = user.getAvatar();
        Picasso.get().load(imageUrl).placeholder(R.drawable.default_avatar).into(ivUserAvatar);

        etUserName.setText(user.getName());
        etUserEmail.setText(user.getEmail());
        etUserPassword.setText(user.getPassword());

        tvUserId.setText(String.valueOf(user.getId()));
    }

    private void modifyUserProfile(int userId, String newName, String newEmail, String newPassword) {
        UserUpdateRequest updatedUser = new UserUpdateRequest(newName, newEmail, newPassword);

        RetrofitService retrofitService = NetworkManager.getRetrofitInstance().create(RetrofitService.class);
        Call<User> call = retrofitService.modifyUserProfile(userId, updatedUser);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(UserActivity.this, "Informations modifiées avec succès", Toast.LENGTH_SHORT).show();

                    updateUI(response.body());

                } else {
                    Toast.makeText(UserActivity.this, "Erreur de modification des informations de l'utilisateur", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(UserActivity.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        // Supprimer les tokens du SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("access_token");
        editor.remove("refresh_token");
        editor.apply();

        // Rediriger vers la page LoginActivity
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
