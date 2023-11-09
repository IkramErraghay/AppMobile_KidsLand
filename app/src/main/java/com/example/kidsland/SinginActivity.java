package com.example.kidsland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SinginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);


        Button signInButton = findViewById(R.id.signinbutton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from the EditText fields
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SinginActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else {

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SinginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign-in was successful
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(SinginActivity.this, "Sign-in success.", Toast.LENGTH_SHORT).show();


                                        Intent intent = new Intent(SinginActivity.this, CategorieActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // Sign-in failed
                                        Toast.makeText(SinginActivity.this, "Sign-in failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
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


        //pour forgot password
        TextView forgotPasswordTextView = findViewById(R.id.forgotpasswordTextView);
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérez l'e-mail saisi par l'utilisateur
                String email = editTextEmail.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SinginActivity.this, "Please enter your email address.", Toast.LENGTH_SHORT).show();
                } else {
                    // Utilisez la fonction de réinitialisation de mot de passe de Firebase
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SinginActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SinginActivity.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }





}