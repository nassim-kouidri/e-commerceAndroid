package com.esiea.ecommerce.nassim;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.esiea.ecommerce.nassim.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Product> productList;
    private final OnProductClickListener onProductClickListener;

    public interface OnProductClickListener {
        void onProductClick(int position);
    }

    public ProductAdapter(List<Product> productList, OnProductClickListener onProductClickListener) {
        this.productList = productList;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view, onProductClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        String imageUrl = "https://decizia.com/blog/wp-content/uploads/2017/06/default-placeholder.png ";
        List<String> images = product.getImages();
        if (images != null && !images.isEmpty()) {
            imageUrl = images.get(0);
        }

        // Charger l'image avec Picasso
        Picasso.get().load(imageUrl).placeholder(R.drawable.placeholder_image).into(holder.productImage);
        holder.productTitle.setText(product.getTitle());
        holder.productPrice.setText("Price: $" + product.getPrice());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView productImage;
        TextView productTitle, productPrice;
        OnProductClickListener onProductClickListener;

        public ProductViewHolder(@NonNull View itemView, OnProductClickListener onProductClickListener) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            this.onProductClickListener = onProductClickListener;

            // Ajouter un écouteur de clic à l'élément entier
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onProductClickListener.onProductClick(getAdapterPosition());
        }
    }
}
