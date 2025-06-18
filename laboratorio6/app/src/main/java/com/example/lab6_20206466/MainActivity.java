package com.example.lab6_20206466;

import android.os.Bundle;
import com.google.firebase.FirebaseApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Cargar fragmento por defecto
        loadFragment(new Linea1Fragment());

        // Listener con if-else
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.linea1) {
                selectedFragment = new Linea1Fragment();
            } else if (itemId == R.id.limaPass) {
                selectedFragment = new LimaPassFragment();
            } else if (itemId == R.id.resumen) {
                selectedFragment = new ResumenFragment();
            } else if (itemId == R.id.logout) {
                selectedFragment = new LogoutFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            } else {
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, fragment);
        ft.commit();
    }
}

