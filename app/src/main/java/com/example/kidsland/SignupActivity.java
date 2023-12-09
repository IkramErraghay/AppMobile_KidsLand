package com.example.kidsland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kidsland.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText editTextEmail, editTextFullName, editTextPassword, editTextConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize mAuth and mDatabase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");


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

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        // Listener for the Register button
        Button registerButton = findViewById(R.id.registerbutton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String fullName = editTextFullName.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(fullName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                } else {
                    // Use Firebase Auth to create a new user account
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Registration was successful
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        saveUserToDatabase(user.getUid(), email, fullName, password);
                                        Toast.makeText(SignupActivity.this, "Registration success", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(SignupActivity.this, SinginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // Registration failed
                                        Toast.makeText(SignupActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("FirebaseError", task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        });
    }

    private void saveUserToDatabase(String userId, String email, String fullName,String password) {
        User user = new User(email, fullName, password);
        user.setPassword(password);
        mDatabase.child(userId).setValue(user);
    }
}