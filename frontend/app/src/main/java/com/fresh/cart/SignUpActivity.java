package com.fresh.cart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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

public class SignUpActivity extends AppCompatActivity  {

    EditText first_name, last_name, phone_number, type_role, email, password, confirm_password;
    Button sign_up_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        // Hook Edit Text Fields:

        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        type_role = findViewById(R.id.type_role);
        phone_number = findViewById(R.id.phone_number);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);

        // Hook Sign up Button:

        sign_up_btn = findViewById(R.id.sign_up_btn);
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processFormFields();
            }
        });
    }
    //End of OnCreate Method

    public void goToHome(View view) {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToSignInAct(View view) {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public void processFormFields() {
        if (!validateFirstName() || !validateLastName() || !validatePhoneNumber() || !validateroleType() || !validateEmail()
                || !validatePassword() || !validatePasswordConfirm()) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);

        String url = "http://192.168.0.180:8080/auth/inscription";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("succes")) {
                            //textView.setText("Response is: " + response.substring(0, 500));
                            first_name.setText(null);
                            last_name.setText(null);
                            phone_number.setText(null);
                            type_role.setText(null);
                            email.setText(null);
                            password.setText(null);
                            confirm_password.setText(null);

                            Toast.makeText(SignUpActivity.this, "Félicitations ! L'inscription est réussie."
                                    , Toast.LENGTH_LONG).show();
                            //I can add that if all set the client see a page with congratulations or something
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.fillInStackTrace();
                System.out.println(error.getMessage());
                Toast.makeText(SignUpActivity.this, "Échec de l'inscription. Veuillez réessayer.."
                        , Toast.LENGTH_LONG).show();
            }
        }){
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nom", first_name.getText().toString());
                params.put("prenom", last_name.getText().toString());
                params.put("courriel", email.getText().toString());
                params.put("telephone", phone_number.getText().toString());
                params.put("role", type_role.getText().toString());
                params.put("motDePasse", password.getText().toString());

                return params;
            }
        };
        queue.add(stringRequest);
    }

    public boolean validateFirstName() {
        String firstName = first_name.getText().toString();

        if (firstName.isEmpty()) {
            first_name.setError("Le champs 'Nom' ne peut pas être vide!");
            return false;
        } else {
            first_name.setError(null);
            return true;
        }
    }

    public boolean validateLastName() {
        String lastName = last_name.getText().toString();

        if (lastName.isEmpty()) {
            last_name.setError("Le champs 'Prenom' ne peut pas être vide!");
            return false;
        } else {
            last_name.setError(null);
            return true;
        }
    }

    public boolean validatePhoneNumber() {
        Editable phoneNumber = phone_number.getText();

        if (phoneNumber.length() < 9 || phoneNumber.length() > 12) {
            phone_number.setError("Merci de rentrez un numero de telephone valide!");
            return false;
        } else if (phoneNumber.length() == 0) {
            phone_number.setError("Merci de rentrez un numéro de telephone!");
            return false;
        } else {
            phone_number.setError(null);
            return true;
        }
    }

    public boolean validateroleType() {
        String roleType = type_role.getText().toString().toLowerCase().trim();

        if (roleType.isEmpty()) {
            type_role.setError("Merci d'indiquer si vous êtes Client ou Producteur!");
            return false;
        } else if (!roleType.equals("client") && !roleType.equals("producteur")) {
            type_role.setError("Le type de client doit être Client ou Producteur!");
            return false;
        }else {
            type_role.setError(null);
            return true;
        }
    }

    public boolean validateEmail() {
        String e_mail = email.getText().toString();

        if (e_mail.isEmpty()) {
            email.setError("Le champs 'Adresse courriel' ne peut pas être vide!");
            return false;
        } else if (!StringHelper.regexEmailValidationPattern(e_mail)) {
            email.setError("Merci de rentrez une adresse courriel valide");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String pass_word = password.getText().toString();

        if (pass_word.isEmpty()) {
            password.setError("Merci d'insérez votre mot de passe!");
            return false;
        } else  {
            password.setError(null);
            return true;
        }
    }

    public boolean validatePasswordConfirm() {
        String confirm_pass_word = confirm_password.getText().toString();

        if (confirm_pass_word.isEmpty()) {
            confirm_password.setError("Merci de confirmez votre mot de passe!");
            return false;
        } else if (!confirm_pass_word.equals(password.getText().toString())) {
            confirm_password.setError("Les mots de passe ne correspondent pas.!");
            return false;
        } else  {
            confirm_password.setError(null);
            return true;
        }
    }
}
