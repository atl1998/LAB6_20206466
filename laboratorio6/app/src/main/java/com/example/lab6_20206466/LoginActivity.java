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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private Button googleLoginButton;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

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

            if (user != null) {
                String uid = user.getUid();
                String nombre = user.getDisplayName();
                String correo = user.getEmail();

                // Guardar en Firestore
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("nombre", nombre);
                userMap.put("correo", correo);

                firestore.collection("usuarios")
                        .document(uid)
                        .set(userMap)
                        .addOnSuccessListener(unused ->
                                Log.d(TAG, "Usuario guardado en Firestore"))
                        .addOnFailureListener(e ->
                                Log.e(TAG, "Error al guardar usuario en Firestore", e));
            }

            // Ir a MainActivity
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Log.d(TAG, "Inicio cancelado o fallido.");
        }
    }
}
