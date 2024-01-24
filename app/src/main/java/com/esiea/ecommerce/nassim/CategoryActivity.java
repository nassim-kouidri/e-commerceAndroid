package com.esiea.ecommerce.nassim;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esiea.ecommerce.nassim.model.Category;
import com.esiea.ecommerce.nassim.network.NetworkManager;
import com.esiea.ecommerce.nassim.network.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        recyclerView = findViewById(R.id.recyclerView);

        // Configurer la RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchData();

        BottomNavigationHelper.setupBottomNavigation(this, R.id.navigation_product);
    }

    private void fetchData() {
        RetrofitService retrofitService = NetworkManager.getRetrofitInstance().create(RetrofitService.class);
        Call<List<Category>> call = retrofitService.getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body();

                    // Créer l'adaptateur
                    CategoryAdapter adapter = new CategoryAdapter(categoryList);

                    // Configurer la RecyclerView avec l'adaptateur
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(CategoryActivity.this, "Erreur de chargement des catégories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                Toast.makeText(CategoryActivity.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
