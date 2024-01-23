// ProductDetailActivity.java
package com.esiea.ecommerce.nassim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.esiea.ecommerce.nassim.model.Product;
import com.esiea.ecommerce.nassim.network.NetworkManager;
import com.esiea.ecommerce.nassim.network.RetrofitService;
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

        // Récupérer l'id du produit sélectionné depuis l'intent
        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);

        // Vérifier si l'id est valide
        if (productId != -1) {
            // Appel à l'API pour récupérer les détails du produit en fonction de son id
            fetchProductDetails(productId);
        } else {
            // Id invalide, peut-être afficher un message d'erreur
            Toast.makeText(this, "Produit non valide", Toast.LENGTH_SHORT).show();
            finish();  // Fermer l'activité si l'id n'est pas valide
        }

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, ProductActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void fetchProductDetails(int productId) {
        // Appel à l'API pour récupérer les détails du produit en fonction de son id
        RetrofitService retrofitService = NetworkManager.getRetrofitInstance().create(RetrofitService.class);
        Call<Product> call = retrofitService.getProductDetails(productId);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Succès de la récupération des détails du produit
                    Product product = response.body();

                    // Mettre à jour l'interface utilisateur avec les détails du produit
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
                finish();  // Fermer l'activité en cas d'erreur
            }
        });
    }

    private void updateUI(Product product) {
        // Mettre à jour l'interface utilisateur avec les détails du produit
        String imageUrl = "https://decizia.com/blog/wp-content/uploads/2017/06/default-placeholder.png ";
        List<String> images = product.getImages();
        if (images != null && !images.isEmpty()) {
            imageUrl = images.get(0);
        }

        // Charger l'image avec Picasso
        Picasso.get().load(imageUrl).placeholder(R.drawable.placeholder_image).into(imgProductDetail);
        tvProductName.setText(product.getTitle());
        tvProductDescription.setText(product.getDescription());
    }
}
