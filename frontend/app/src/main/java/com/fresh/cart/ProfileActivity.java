package com.fresh.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {


    TextView tv_first_name, tv_last_name, tv_phone_number, tv_email, tv_type_role;
    Button sign_out_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        tv_first_name = findViewById(R.id.first_name);
        tv_last_name = findViewById(R.id.last_name);
        tv_phone_number = findViewById(R.id.phone_number);
        tv_email = findViewById(R.id.email);
        tv_type_role = findViewById(R.id.type_role);

        String first_name = getIntent().getStringExtra("first_name");
        String last_name = getIntent().getStringExtra("last_name");
        String email = getIntent().getStringExtra("email");
        String phoneNumber = getIntent().getStringExtra("phone_number");
        String role = getIntent().getStringExtra("type_role");

        assert first_name != null;
        assert last_name != null;
        assert role != null;
        tv_first_name.setText(first_name.toUpperCase());
        tv_last_name.setText(last_name.toUpperCase());
        tv_phone_number.setText(phoneNumber);
        tv_email.setText(email);
        tv_type_role.setText(role.toUpperCase());

        sign_out_btn = findViewById(R.id.sign_out_btn);

        sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUserOut();
            }
        });

    }

    public void signUserOut() {
        tv_first_name.setText(null);
        tv_last_name.setText(null);
        tv_phone_number.setText(null);
        tv_email.setText(null);
        tv_type_role.setText(null);

        Intent goToHome = new Intent(ProfileActivity.this, MainActivity.class);
        goToHome.putExtra("logout", "Déconnexion de votre compte effectuée avec succès.");
        startActivity(goToHome);
        finish();
    }
}
