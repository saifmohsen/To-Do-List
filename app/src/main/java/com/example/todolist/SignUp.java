package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {
    private TextView toLoginTxt;
    private Button signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {

        }

        toLoginTxt = (TextView) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        toLoginTxt.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              Intent intent = new Intent(getApplicationContext(), Login.class);
                                              startActivity(intent);
                                          }
                                      }
        );

        signup.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          Intent intent = new Intent(getApplicationContext(), Login.class);
                                          startActivity(intent);
                                      }
                                  }
        );
    }


}