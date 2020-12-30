package com.example.todolist2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist2.utilities.Helpers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {


    EditText emaill, passwordd;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {

        }

        firebaseAuth = FirebaseAuth.getInstance();

        emaill = findViewById(R.id.email);
        passwordd = findViewById(R.id.password);


        Button btn_Login = findViewById(R.id.login);
        btn_Login.setOnClickListener(view -> {
            Helpers.HideKeyboard(Login.this);

            String email = emaill.getText().toString().trim();
            String password = passwordd.getText().toString().trim();
            if (email.isEmpty()) {
                emaill.setError("Please enter email");
                return;
            }
            if (password.isEmpty()) {
                passwordd.setError("Please enter password");
                return;
            }

            btn_Login.setEnabled(false);
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        btn_Login.setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Home.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        TextView tv_register = findViewById(R.id.create_account);
        tv_register.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
            finish();
        }
    }
}