package com.example.lab6_20206466;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        //iniciar sesion con correo y contraseña
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton); // Botón manual de login

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = authResult.getUser();
                        if (user != null) {
                            Log.d(TAG, "Login exitoso con correo: " + user.getEmail());
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error al iniciar sesión", e);
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });


        //registro nuevos usuarios
        TextView registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });


        //iniciar sesion cn google
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
