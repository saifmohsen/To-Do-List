package com.example.todolist2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist2.utilities.Helpers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private TextView toLoginTxt;

    EditText emaill, namee, passwordd;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sign_up);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {

        }


        firebaseAuth = FirebaseAuth.getInstance();
        emaill = findViewById(R.id.email);
        namee = findViewById(R.id.name);
        passwordd = findViewById(R.id.password);


        Button btn_Register = findViewById(R.id.signup);
        btn_Register.setOnClickListener(view -> {
            Helpers.HideKeyboard(SignUp.this);

            String email = emaill.getText().toString().trim();
            String name = namee.getText().toString().trim();
            String password = passwordd.getText().toString().trim();
            if (email.isEmpty()) {
                emaill.setError("Please enter email");
                return;
            }
            if (name.isEmpty()) {
                namee.setError("Please enter name");
                return;
            }
            if (password.isEmpty()) {
                passwordd.setError("Please enter password");
                return;
            }

            btn_Register.setEnabled(false);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {

                        btn_Register.setEnabled(true);
                        if (task.isSuccessful()) {

                            currentUser = firebaseAuth.getCurrentUser();
                            String uid = currentUser.getUid();
                            Map<String, Object> data = new HashMap<>();
                            data.put("uid", uid);
                            data.put("name", name);
                            FirebaseDatabase.getInstance().getReference("Users").child(uid).setValue(data)
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(SignUp.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUp.this, Home.class);
                                        startActivity(intent);
                                        finish();
                                    });

                        } else {
                            Toast.makeText(SignUp.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        TextView tv_login = findViewById(R.id.tologin);
        tv_login.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
            finish();
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
            finish();
        }
    }


}