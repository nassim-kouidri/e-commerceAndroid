package com.esiea.ecommerce.nassim.utils;

import android.app.Activity;
import android.content.Intent;

import com.esiea.ecommerce.nassim.R;
import com.esiea.ecommerce.nassim.admin.ProductAdminActivity;
import com.esiea.ecommerce.nassim.admin.UserAdminActivity;
import com.esiea.ecommerce.nassim.customer.CategoryActivity;
import com.esiea.ecommerce.nassim.customer.ProductActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationAdminHelper {

    public static void setupBottomNavigationAdmin(Activity activity, int selectedItemId) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomNavigationAdminView);
        bottomNavigationView.setSelectedItemId(selectedItemId);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_products) {
                if (!(activity instanceof ProductActivity)) {
                    navigateToActivity(activity, ProductAdminActivity.class);
                }
                return true;
            } else if (itemId == R.id.navigation_users) {
                if (!(activity instanceof CategoryActivity)) {
                    navigateToActivity(activity, UserAdminActivity.class);
                }
                return true;
            } else return itemId == R.id.navigation_create_product;
        });
    }

    private static void navigateToActivity(Activity sourceActivity, Class<? extends Activity> targetActivityClass) {
        Intent intent = new Intent(sourceActivity, targetActivityClass);
        sourceActivity.startActivity(intent);
        sourceActivity.finish();
    }
}
