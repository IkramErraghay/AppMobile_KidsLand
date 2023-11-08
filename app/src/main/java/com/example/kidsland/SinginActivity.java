package com.example.kidsland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SinginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);

        // Configurer le bouton Sign In pour qu'il soit cliquable
        Button signInButton = findViewById(R.id.signinbutton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer un Intent pour démarrer CategorieActivity
                Intent intent = new Intent(SinginActivity.this, CategorieActivity.class);
                startActivity(intent);
            }
        });

        // Configuration pour "Don't have an account ? Sign Up" TextView
        TextView dontHaveAccountTextView = findViewById(R.id.dontHaveAccountTextView);
        dontHaveAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer un Intent pour démarrer SignupActivity
                Intent intent = new Intent(SinginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
