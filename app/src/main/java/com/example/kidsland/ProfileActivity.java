package com.example.kidsland;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidsland.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase components
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Get the reference of the user name TextView
        userNameTextView = findViewById(R.id.userName);

        // Fetch and display the user's name
        fetchAndDisplayUserName();
        // When the edit icon is clicked
        ImageView editUserIcon = findViewById(R.id.editUserIcon);
        editUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event for the edit icon

                // Fetch the current user information
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    String uid = currentUser.getUid();
                    DatabaseReference userReference = databaseReference.child(uid);

                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                // Pass the user information to showEditUserDialog
                                showEditUserDialog(user.getFullName(), user.getEmail());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });
                }
            }
        });

        // Créez une liste d'exemple d'éléments de compte
        List<AccountItem> accountItemList = new ArrayList<>();
        accountItemList.add(new AccountItem(R.drawable.child, "Child 1"));
        accountItemList.add(new AccountItem(R.drawable.child, "Child 2"));
        accountItemList.add(new AccountItem(R.drawable.child, "Child 3"));
        accountItemList.add(new AccountItem(R.drawable.add_icon, "New Account"));

        // Obtenez la référence du RecyclerView à partir de votre mise en page
        RecyclerView recyclerView = findViewById(R.id.accountsRecyclerView);

        // Créez un adaptateur pour les éléments de compte
        AccountAdapter accountAdapter = new AccountAdapter(accountItemList, R.layout.item_account);

        // Configurez le RecyclerView avec un gestionnaire de disposition horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Définissez l'adaptateur sur le RecyclerView
        recyclerView.setAdapter(accountAdapter);

        // Créez une liste d'exemple d'éléments de téléchargement
        List<DownloadItem> downloadItemList = new ArrayList<>();
        downloadItemList.add(new DownloadItem(R.drawable.film1, "Download 1"));
        downloadItemList.add(new DownloadItem(R.drawable.film2, "Download 2"));
        downloadItemList.add(new DownloadItem(R.drawable.film3, "Download 3"));
        downloadItemList.add(new DownloadItem(R.drawable.add_icon, "New Film"));

        // Obtenez la référence du RecyclerView pour la section "My Selection"
        RecyclerView recyclerView2 = findViewById(R.id.accountsRecyclerView2);

        // Créez un adaptateur pour les éléments de téléchargement
        DownloadAdapter downloadAdapter = new DownloadAdapter(downloadItemList, R.layout.item_downloads);

        // Configurez le RecyclerView avec un gestionnaire de disposition horizontal
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        // Définissez l'adaptateur sur le RecyclerView
        recyclerView2.setAdapter(downloadAdapter);
    }

    private void fetchAndDisplayUserName() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userReference = databaseReference.child(uid);

            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        String fullName = user.getFullName();
                        userNameTextView.setText(fullName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    // New method to show the edit user dialog
    private void showEditUserDialog(String currentUserName, String currentUserEmail) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_edit_user);

        EditText editTextName = dialog.findViewById(R.id.editTextFullName);
        EditText editTextEmail = dialog.findViewById(R.id.editTextEmail);
        EditText editTextPassword = dialog.findViewById(R.id.editTextPassword);
        Button buttonSave = dialog.findViewById(R.id.buttonSave);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

        editTextName.setText(currentUserName);
        editTextEmail.setText(currentUserEmail);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the saveChanges method with the necessary parameters
                saveChanges(editTextName.getText().toString(), editTextEmail.getText().toString(), editTextPassword.getText().toString());

                // Dismiss the dialog after saving changes
                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the dialog without saving changes
                dialog.dismiss();
            }
        });

        dialog.show();
    }



    private void saveChanges(String updatedName, String updatedEmail, String updatedPassword) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(uid);

            // Update user name and email
            userReference.child("fullName").setValue(updatedName);
            userReference.child("email").setValue(updatedEmail);
            userReference.child("password").setValue(updatedPassword);

            // Display a message or perform any other necessary actions
            Toast.makeText(this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
        }
    }

}
