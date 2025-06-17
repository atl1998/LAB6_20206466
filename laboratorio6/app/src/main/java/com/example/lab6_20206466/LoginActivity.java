package com.example.lab6_20206466;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // Botón
    private Button googleLoginButton;

    // Lista de proveedores
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    // Activity Result Launcher
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        googleLoginButton = findViewById(R.id.googleLoginButton);

        googleLoginButton.setOnClickListener(v -> {
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);
        });
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Log.d(TAG, "UID: " + user.getUid());
            Log.d(TAG, "Correo: " + user.getEmail());

            // Ir a MainActivity
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Log.d(TAG, "Inicio cancelado o fallido.");
        }
    }
}
