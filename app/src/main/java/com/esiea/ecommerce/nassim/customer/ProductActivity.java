package com.esiea.ecommerce.nassim.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esiea.ecommerce.nassim.R;
import com.esiea.ecommerce.nassim.model.Product;
import com.esiea.ecommerce.nassim.network.NetworkManager;
import com.esiea.ecommerce.nassim.network.RetrofitService;
import com.esiea.ecommerce.nassim.utils.BottomNavigationHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductAdapter adapter;
    private RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Initialiser retrofitService
        retrofitService = NetworkManager.getRetrofitInstance().create(RetrofitService.class);

        // Configurer la RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Charger tous les produits initialement
        fetchData("");

        // Configurer la Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialiser le Bottom NavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> true);

        // Configurer la Bottom Navigation
        BottomNavigationHelper.setupBottomNavigation(this, R.id.navigation_product);
    }

    private void fetchData(String title) {
        Call<List<Product>> call = title.isEmpty() ? retrofitService.getProducts() : retrofitService.searchProducts(title);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList = response.body();

                    // Créer l'adaptateur
                    adapter = new ProductAdapter(productList, ProductActivity.this);

                    // Configurer la RecyclerView avec l'adaptateur
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ProductActivity.this, "Erreur de chargement des produits", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                Toast.makeText(ProductActivity.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onProductClick(int position) {
        int productId = productList.get(position).getId();
        Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_ID", productId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        assert searchView != null;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchData(newText);
                return false;
            }
        });

        return true;
    }
}
