package com.esiea.ecommerce.nassim.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.esiea.ecommerce.nassim.R;
import com.esiea.ecommerce.nassim.model.Product;
import com.esiea.ecommerce.nassim.network.NetworkManager;
import com.esiea.ecommerce.nassim.network.RetrofitService;
import com.esiea.ecommerce.nassim.utils.BottomNavigationHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProductDetail;
    private TextView tvProductName, tvProductDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imgProductDetail = findViewById(R.id.imgProductDetail);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductDescription = findViewById(R.id.tvProductDescription);

        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);

        if (productId != -1) {
            fetchProductDetails(productId);
        } else {
            Toast.makeText(this, "Produit non valide", Toast.LENGTH_SHORT).show();
            finish();
        }

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, ProductActivity.class);
            startActivity(intent);
            finish();
        });

        BottomNavigationHelper.setupBottomNavigation(this, R.id.navigation_product);

    }

    private void fetchProductDetails(int productId) {
        RetrofitService retrofitService = NetworkManager.getRetrofitInstance().create(RetrofitService.class);
        Call<Product> call = retrofitService.getProductDetails(productId);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body();
                    updateUI(product);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Erreur de chargement des détails du produit", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                // Erreur réseau
                Toast.makeText(ProductDetailActivity.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateUI(Product product) {
        String imageUrl = "https://decizia.com/blog/wp-content/uploads/2017/06/default-placeholder.png ";
        List<String> images = product.getImages();
        if (images != null && !images.isEmpty()) {
            imageUrl = images.get(0);
        }

        Picasso.get().load(imageUrl).placeholder(R.drawable.placeholder_image).into(imgProductDetail);
        tvProductName.setText(product.getTitle());
        tvProductDescription.setText(product.getDescription());
    }
}
