package com.esiea.ecommerce.nassim;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Utiliser un délai pour afficher la page de connexion après un court instant
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Rediriger vers la page de connexion
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();  // Pour empêcher l'utilisateur de revenir à cette activité avec le bouton "retour"
            }
        }, 1000);  // Délai de 1000 millisecondes (1 seconde)
    }
}
