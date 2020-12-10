package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private TextView toSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {

        }


        toSignUp = (TextView) findViewById(R.id.create_account);
        toSignUp.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(getApplicationContext(), SignUp.class);
                                            startActivity(intent);
                                        }
                                    }
        );
    }
}