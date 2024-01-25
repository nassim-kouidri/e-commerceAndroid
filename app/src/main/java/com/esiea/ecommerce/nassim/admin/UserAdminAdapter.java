package com.esiea.ecommerce.nassim.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.esiea.ecommerce.nassim.R;
import com.esiea.ecommerce.nassim.model.User;
import com.esiea.ecommerce.nassim.network.NetworkManager;
import com.esiea.ecommerce.nassim.network.RetrofitService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdminAdapter extends RecyclerView.Adapter<UserAdminAdapter.UserViewHolder> {

    private final List<User> userList;
    private final OnUserClickListener onUserClickListener;

    public interface OnUserClickListener {
        void onUserClick(int position);
    }

    public UserAdminAdapter(List<User> userList, OnUserClickListener onUserClickListener) {
        this.userList = userList;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view, onUserClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.userName.setText(user.getName());

        Picasso.get().load(user.getAvatar()).placeholder(R.drawable.placeholder_image).into(holder.userAvatar);

        holder.btnDelete.setOnClickListener(v -> {
            int productId = userList.get(holder.getAdapterPosition()).getId();
            deleteUser(productId, holder.getAdapterPosition());
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    private void deleteUser(int userId, int position) {
        RetrofitService retrofitService = NetworkManager.getRetrofitInstance().create(RetrofitService.class);
        Call<Void> call = retrofitService.deleteUsers(userId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    userList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, userList.size());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }

    static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView userAvatar;
        TextView userName;
        OnUserClickListener onUserClickListener;
        Button btnEdit, btnDelete;

        public UserViewHolder(@NonNull View itemView, OnUserClickListener onUserClickListener) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.userAvatar);
            userName = itemView.findViewById(R.id.userName);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            this.onUserClickListener = onUserClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onUserClickListener.onUserClick(getAdapterPosition());
        }
    }
}
