package com.esiea.ecommerce.nassim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esiea.ecommerce.nassim.model.Product;
import com.esiea.ecommerce.nassim.network.NetworkManager;
import com.esiea.ecommerce.nassim.network.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerView;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        recyclerView = findViewById(R.id.recyclerView);

        // Configurer la RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Appel Retrofit pour récupérer les produits
        fetchData();
    }

    private void fetchData() {
        RetrofitService retrofitService = NetworkManager.getRetrofitInstance().create(RetrofitService.class);
        Call<List<Product>> call = retrofitService.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList = response.body();

                    // Créer l'adaptateur
                    ProductAdapter adapter = new ProductAdapter(productList, ProductActivity.this);

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
        // Récupérer l'ID du produit sélectionné
        int productId = productList.get(position).getId();

        // Naviguer vers l'écran de détail du produit en passant l'ID du produit
        Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_ID", productId);  // Utiliser la même clé que dans ProductDetailActivity
        startActivity(intent);
    }

}
