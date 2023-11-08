package com.example.kidsland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Listener for the sign-in text
        TextView signInText = findViewById(R.id.alreadyHaveAccountTextView);
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to navigate to the SignInActivity
                Intent intent = new Intent(SignupActivity.this, SinginActivity.class);
                startActivity(intent);
            }
        });

        // Listener for the Register button
        Button registerButton = findViewById(R.id.registerbutton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the registration logic here
                // After registration, you can start the SignInActivity
                // For now, we just navigate to the SignInActivity
                Intent intent = new Intent(SignupActivity.this, SinginActivity.class);
                startActivity(intent);
            }
        });
    }

}