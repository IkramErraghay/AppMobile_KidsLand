package com.example.kidsland;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUserActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        editTextName = findViewById(R.id.editTextFullName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        // Retrieve the user information passed from ProfileActivity
        String currentUserName = getIntent().getStringExtra("USER_NAME");

        // Initialize your EditText views
        editTextName.setText(currentUserName);

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });
    }

    // Method called when the Save button is clicked
    private void saveChanges() {
        // Get the edited information from the EditText
        String updatedName = editTextName.getText().toString();
        String updatedEmail = editTextEmail.getText().toString();
        String updatedPassword = editTextPassword.getText().toString();

        // Update the user's information in the database
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // Update user name
            DatabaseReference userReference = databaseReference.child(currentUser.getUid());
            userReference.child("fullName").setValue(updatedName);

            // Check if the email is being updated
            if (!updatedEmail.isEmpty() && !currentUser.getEmail().equals(updatedEmail)) {
                // Reauthenticate to update email
                currentUser.reauthenticate(EmailAuthProvider.getCredential(currentUser.getEmail(), updatedPassword))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Update email
                                currentUser.updateEmail(updatedEmail).addOnCompleteListener(emailTask -> {
                                    if (emailTask.isSuccessful()) {
                                        Toast.makeText(EditUserActivity.this, "Email updated successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(EditUserActivity.this, "Failed to update email", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(EditUserActivity.this, "Authentication failed. Check your password.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            // Check if the password is being updated
            if (!updatedPassword.isEmpty()) {
                // Update password
                currentUser.updatePassword(updatedPassword).addOnCompleteListener(passwordTask -> {
                    if (passwordTask.isSuccessful()) {
                        Toast.makeText(EditUserActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditUserActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // Finish the activity to return to the ProfileActivity
            finish();
        }
    }
}
