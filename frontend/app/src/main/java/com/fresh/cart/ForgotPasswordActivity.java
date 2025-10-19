package com.fresh.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fresh.cart.helpers.StringHelper;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText et_email;
    Button forgot_pass_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);

        et_email = findViewById(R.id.email_to_tecover);

        forgot_pass_btn = findViewById(R.id.forgot_pass_btn);

        forgot_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMailRecoverPassword();
            }
        });
    }

    public void sendMailRecoverPassword() {
        if (!validateEmail()) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(ForgotPasswordActivity.this);

        String url = "http://192.168.0.180:8080/auth/mot-de-passe-oublie";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("succes")) {
                            //textView.setText("Response is: " + response.substring(0, 500));
                            et_email.setText(null);

                            Toast.makeText(ForgotPasswordActivity.this, "Un e-mail de réinitialisation a été envoyé."
                                    , Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.fillInStackTrace();
                System.out.println(error.getMessage());
                Toast.makeText(ForgotPasswordActivity.this, "Échec d'envoyer un message de recuperation de mot de passe. Veuillez réessayer.."
                        , Toast.LENGTH_LONG).show();
            }
        }){
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("courriel", et_email.getText().toString());

                return params;
            }
        };
        queue.add(stringRequest);


    }


    public void goToSignIn(View view) {
        Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean validateEmail() {
        String mail = et_email.getText().toString();

        if (mail.isEmpty()) {
            et_email.setError("Le champs 'Adresse courriel' ne peut pas être vide!");
            return false;
        } else if (!StringHelper.regexEmailValidationPattern(mail)) {
            et_email.setError("Merci de rentrez une adresse courriel valide");
            return false;
        } else {
            et_email.setError(null);
            return true;
        }
    }
}
