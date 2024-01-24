package com.esiea.ecommerce.nassim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;

import com.esiea.ecommerce.nassim.CategoryActivity;
import com.esiea.ecommerce.nassim.ProductActivity;
import com.esiea.ecommerce.nassim.R;
import com.esiea.ecommerce.nassim.UserActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationHelper {

    public static void setupBottomNavigation(Activity activity, int selectedItemId) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(selectedItemId);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_product) {
                if (!(activity instanceof ProductActivity)) {
                    navigateToActivity(activity, ProductActivity.class);
                }
                return true;
            } else if (itemId == R.id.navigation_category) {
                if (!(activity instanceof CategoryActivity)) {
                    navigateToActivity(activity, CategoryActivity.class);
                }
                return true;
            } else if (itemId == R.id.navigation_user) {
                if (!(activity instanceof UserActivity)) {
                    navigateToActivity(activity, UserActivity.class);
                }
                return true;
            }
            return false;
        });
    }

    private static void navigateToActivity(Activity sourceActivity, Class<? extends Activity> targetActivityClass) {
        Intent intent = new Intent(sourceActivity, targetActivityClass);
        sourceActivity.startActivity(intent);
        sourceActivity.finish();
    }
}



