package com.fresh.cart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText et_new_password;
    Button btn_reset_password;
    private String token; // Token récupéré depuis l'URL

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        et_new_password = findViewById(R.id.et_new_password);
        btn_reset_password = findViewById(R.id.btn_reset_password);

        // Récupérer le token depuis l'intent
        token = getTokenFromIntent();

        if (token == null) {
            Toast.makeText(this, "Lien invalide ou expiré !", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    // Récupère le token de l'URL
    // Récupère le token de l'URL en supprimant ":8080" si nécessaire
    private String getTokenFromIntent() {
        Uri data = getIntent().getData();
        if (data != null) {
            String url = data.toString();

            // Vérifie et supprime le port ":8080" s'il est présent
            if (url.contains("192.168.0.180:8080")) {
                url = url.replace("192.168.0.180:8080", "192.168.0.180");
            }

            // Extraire le token depuis l'URL corrigée
            Uri uri = Uri.parse(url);
            return uri.getQueryParameter("token");
        }
        return null;
    }


    // Fonction pour envoyer la requête de réinitialisation du mot de passe
    private void resetPassword() {
        final String newPassword = et_new_password.getText().toString();

        if (newPassword.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un mot de passe.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://192.168.0.180:8080/auth/reinitialiser-mot-de-passe";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ResetPasswordActivity.this, "Mot de passe réinitialisé avec succès !", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ResetPasswordActivity.this, SignInActivity.class)); // Redirige vers login
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Erreur de connexion. Veuillez réessayer.";

                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 400) {
                        errorMessage = "Token invalide ou expiré.";
                    } else if (error.networkResponse.statusCode == 500) {
                        errorMessage = "Erreur serveur. Veuillez réessayer plus tard.";
                    }
                }

                Toast.makeText(ResetPasswordActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("nouveauMotDePasse", newPassword);
                return params;
            }
        };

        queue.add(stringRequest);
    }

}

