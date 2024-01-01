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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        // Récupérez les données passées de ProfileActivity (si elles existent)
        Intent intent = getIntent();
        String newName = intent.getStringExtra("newName");
        String newEmail = intent.getStringExtra("newEmail");
        String newPassword = intent.getStringExtra("newPassword");

        Button signInButton = findViewById(R.id.signinbutton);
        signInButton.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(SinginActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(SinginActivity.this, "Sign-in success.", Toast.LENGTH_SHORT).show();
                                if (newEmail != null && newPassword != null) {
                                    // Mettez à jour le profil de l'utilisateur après la re-authentification
                                    updateProfile(newName, newEmail, newPassword);
                                } else {
                                    // Sinon, continuez vers CategorieActivity
                                    Intent categoryIntent = new Intent(SinginActivity.this, CategorieActivity.class);
                                    startActivity(categoryIntent);
                                }
                            } else {
                                Toast.makeText(SinginActivity.this, "Sign-in failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
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


    private void updateProfile(String newName, String newEmail, String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Update email
            user.updateEmail(newEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Update name and email in the database
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                            userRef.child("fullName").setValue(newName);
                            userRef.child("email").setValue(newEmail);

                            if (!newPassword.isEmpty()) {
                                // If a new password is provided, update it
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                // Update the password in the database
                                                userRef.child("password").setValue(newPassword);
                                                Toast.makeText(this, "Profile and password updated successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                // If no new password is provided, show success message for profile update only
                                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            }

                            // Redirect to CategorieActivity
                            Intent categoryIntent = new Intent(SinginActivity.this, CategorieActivity.class);
                            startActivity(categoryIntent);
                        } else {
                            Toast.makeText(this, "Failed to update email", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }



}