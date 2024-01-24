package com.esiea.ecommerce.nassim.network;

import com.esiea.ecommerce.nassim.model.AuthResponse;
import com.esiea.ecommerce.nassim.model.Category;
import com.esiea.ecommerce.nassim.model.LoginRequest;
import com.esiea.ecommerce.nassim.model.Product;
import com.esiea.ecommerce.nassim.model.User;
import com.esiea.ecommerce.nassim.model.UserUpdateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitService {
    @GET("products")
    Call<List<Product>> getProducts();

    @POST("users/")
    Call<User> registerUser(@Body User user);

    @POST("auth/login")
    Call<AuthResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("products/{id}")
    Call<Product> getProductDetails(@Path("id") int productId);

    @GET("categories")
    Call<List<Category>> getCategories();

    @GET("auth/profile")
    Call<User> getUserProfile(@Header("Authorization") String accessToken);

    @PUT("users/{id}")
    Call<User> modifyUserProfile(@Path("id") int userId, @Body UserUpdateRequest modifiedUser);
}
