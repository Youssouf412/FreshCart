package com.fresh.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fresh.cart.helpers.StringHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity  {

    Button sign_in_btn;
    EditText et_email, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);

        //Hook email and password texts
        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);

        sign_in_btn = findViewById(R.id.sign_in_btn);

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });

    }
    //End of OnCreate Method

    public void authenticateUser() {
        if (!validateEmail() || !validatePassword()) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);

        String url = "http://192.168.0.180:8080/auth/connexion";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("courriel", et_email.getText().toString());
        params.put("motDePasse", et_password.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String first_name = (String) response.get("nom");
                            String last_name = (String) response.get("prenom");
                            String email = (String) response.get("courriel");
                            System.out.println(email);
                            String phoneNumber = (String) response.get("telephone");
                            String role = (String) response.get("role");

                            //Pass values to profil activity

                            Intent goToProfile = new Intent(SignInActivity.this, ProfileActivity.class);

                            goToProfile.putExtra("first_name", first_name);
                            goToProfile.putExtra("last_name", last_name);
                            goToProfile.putExtra("phone_number", phoneNumber);
                            goToProfile.putExtra("email", email);
                            goToProfile.putExtra("type_role", role);

                            startActivity(goToProfile);
                            finish();


                        } catch (JSONException e) {
                            e.fillInStackTrace();
                            System.out.println(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.fillInStackTrace();
                System.out.println(error.getMessage());
                Toast.makeText(SignInActivity.this, "Connexion n'est pas établie", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    public void goToHome(View view) {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToSignUpAct(View view) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToforgotPassAct(View view) {
        Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        finish();
    }
    public void goToForgotPassword() {
        Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
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

    public boolean validatePassword() {
        String password = et_password.getText().toString();

        if (password.isEmpty()) {
            et_password.setError("Merci d'insérez votre mot de passe!");
            return false;
        } else  {
            et_password.setError(null);
            return true;
        }
    }
}
