package com.esiea.ecommerce.nassim.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.esiea.ecommerce.nassim.R;
import com.esiea.ecommerce.nassim.model.Product;
import com.esiea.ecommerce.nassim.model.ProductUpdateRequest;
import com.esiea.ecommerce.nassim.network.NetworkManager;
import com.esiea.ecommerce.nassim.network.RetrofitService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

    public class UpdateProductAdminActivity extends AppCompatActivity {

    private EditText etTitle, etPrice, etDescription;
    private ImageView imgProductDetail;
    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product_admin);

        imgProductDetail = findViewById(R.id.imgProductDetail);
        etTitle = findViewById(R.id.etTitle);
        etPrice = findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDescription);
        Button btnUpdate = findViewById(R.id.btnUpdate);

        productId = getIntent().getIntExtra("PRODUCT_ID", -1);

        fetchProductDetails(productId);

        btnUpdate.setOnClickListener(view -> updateProduct());
    }

    private void fetchProductDetails(int productId) {
        RetrofitService retrofitService = NetworkManager.getRetrofitInstance().create(RetrofitService.class);
        Call<Product> call = retrofitService.getProductDetails(productId);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response);
                } else {
                    Toast.makeText(UpdateProductAdminActivity.this, "Erreur de chargement des détails du produit", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                Toast.makeText(UpdateProductAdminActivity.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateUI(@NonNull Response<Product> response) {
        Product product = response.body();
        String imageUrl = "https://decizia.com/blog/wp-content/uploads/2017/06/default-placeholder.png ";
        assert product != null;
        List<String> images = product.getImages();
        if (images != null && !images.isEmpty()) {
            imageUrl = images.get(0);
        }

        Picasso.get().load(imageUrl).placeholder(R.drawable.placeholder_image).into(imgProductDetail);
        etTitle.setText(product.getTitle());
        etPrice.setText(String.valueOf(product.getPrice()));
        etDescription.setText(product.getDescription());
    }

    private void updateProduct() {
        String title = etTitle.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty() || priceStr.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir le prix en int
        int price = Integer.parseInt(priceStr);

        ProductUpdateRequest updateRequest = new ProductUpdateRequest();
        updateRequest.setTitle(title);
        updateRequest.setPrice(price);
        updateRequest.setDescription(description);

        RetrofitService retrofitService = NetworkManager.getRetrofitInstance().create(RetrofitService.class);
        Call<Product> call = retrofitService.updateProduct(productId, updateRequest);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(UpdateProductAdminActivity.this, "Produit mis à jour avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateProductAdminActivity.this, "Erreur de mise à jour du produit", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                Toast.makeText(UpdateProductAdminActivity.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
