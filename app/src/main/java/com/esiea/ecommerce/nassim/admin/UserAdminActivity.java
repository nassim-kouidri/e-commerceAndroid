package com.esiea.ecommerce.nassim.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esiea.ecommerce.nassim.R;
import com.esiea.ecommerce.nassim.model.User;
import com.esiea.ecommerce.nassim.network.NetworkManager;
import com.esiea.ecommerce.nassim.network.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdminActivity extends AppCompatActivity implements UserAdminAdapter.OnUserClickListener {

    private RecyclerView recyclerViewUsers;
    private List<User> userList;
    private UserAdminAdapter adapter;
    private RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);

        retrofitService = NetworkManager.getRetrofitInstance().create(RetrofitService.class);

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        fetchUsers();
    }

    private void fetchUsers() {
        Call<List<User>> call = retrofitService.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userList = response.body();

                    adapter = new UserAdminAdapter(userList, UserAdminActivity.this);

                    recyclerViewUsers.setAdapter(adapter);
                } else {
                    Toast.makeText(UserAdminActivity.this, "Erreur de chargement des utilisateurs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Toast.makeText(UserAdminActivity.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUserClick(int position) {
    }
}
