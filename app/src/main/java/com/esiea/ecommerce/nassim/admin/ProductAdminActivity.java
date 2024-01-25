package com.esiea.ecommerce.nassim.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esiea.ecommerce.nassim.R;
import com.esiea.ecommerce.nassim.model.Product;
import com.esiea.ecommerce.nassim.network.NetworkManager;
import com.esiea.ecommerce.nassim.network.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdminActivity extends AppCompatActivity implements ProductAdminAdapter.OnProductAdminClickListener {

    private RecyclerView recyclerViewAdmin;
    private List<Product> productListAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_admin);

        recyclerViewAdmin = findViewById(R.id.recyclerViewAdmin);

        recyclerViewAdmin.setLayoutManager(new GridLayoutManager(this, 1));

        fetchData();
    }

    private void fetchData() {
        RetrofitService retrofitService = NetworkManager.getRetrofitInstance().create(RetrofitService.class);
        Call<List<Product>> call = retrofitService.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productListAdmin = response.body();

                    ProductAdminAdapter adapter = new ProductAdminAdapter(productListAdmin, ProductAdminActivity.this);

                    recyclerViewAdmin.setAdapter(adapter);
                } else {
                    Toast.makeText(ProductAdminActivity.this, "Erreur de chargement des produits", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                Toast.makeText(ProductAdminActivity.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditClick(int position) {
        int productId = productListAdmin.get(position).getId();

        Intent intent = new Intent(ProductAdminActivity.this, UpdateProductAdminActivity.class);
        intent.putExtra("PRODUCT_ID", productId);
        startActivity(intent);
    }
}
