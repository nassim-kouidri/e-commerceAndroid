package com.esiea.ecommerce.nassim.admin;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.esiea.ecommerce.nassim.R;
import com.esiea.ecommerce.nassim.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ProductAdminViewHolder> {

    private final List<Product> productListAdmin;
    private final OnProductAdminClickListener onProductAdminClickListener;

    public interface OnProductAdminClickListener {
        void onEditClick(int position);
    }

    public ProductAdminAdapter(List<Product> productListAdmin, OnProductAdminClickListener onProductAdminClickListener) {
        this.productListAdmin = productListAdmin;
        this.onProductAdminClickListener = onProductAdminClickListener;
    }

    @NonNull
    @Override
    public ProductAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_admin, parent, false);
        return new ProductAdminViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductAdminViewHolder holder, int position) {
        Product product = productListAdmin.get(position);

        String imageUrl = "https://decizia.com/blog/wp-content/uploads/2017/06/default-placeholder.png";
        List<String> images = product.getImages();
        if (images != null && !images.isEmpty()) {
            imageUrl = images.get(0);
        }

        Picasso.get().load(imageUrl).placeholder(R.drawable.placeholder_image).into(holder.productImageAdmin);

        holder.productTitleAdmin.setText(product.getTitle());
        holder.productPriceAdmin.setText("Price: $" + product.getPrice());

        holder.btnEdit.setOnClickListener(v -> {
            // Appeler la méthode d'interface pour le clic sur le bouton "Edit"
            onProductAdminClickListener.onEditClick(holder.getAdapterPosition());
        });

        holder.btnDelete.setOnClickListener(v -> {
            int productId = productListAdmin.get(holder.getAdapterPosition()).getId();
            deleteProduct(productId, holder.getAdapterPosition());
        });
    }

    private void deleteProduct(int productId, int position) {
        // La logique de suppression reste inchangée
        // ...

        // Notifier le changement à l'adaptateur
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productListAdmin.size());
    }

    @Override
    public int getItemCount() {
        return productListAdmin.size();
    }

    static class ProductAdminViewHolder extends RecyclerView.ViewHolder {

        ImageView productImageAdmin;
        TextView productTitleAdmin, productPriceAdmin;
        Button btnEdit, btnDelete;

        public ProductAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageAdmin = itemView.findViewById(R.id.productImageAdmin);
            productTitleAdmin = itemView.findViewById(R.id.productTitleAdmin);
            productPriceAdmin = itemView.findViewById(R.id.productPriceAdmin);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
