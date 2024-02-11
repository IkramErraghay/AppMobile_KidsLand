package com.example.kidsland;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidsland.Model.ChildAccount;
import com.example.kidsland.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private AccountAdapter accountAdapter;
    private List<AccountItem> accountItemList = new ArrayList<>();

    private static final int GALLERY_REQUEST_CODE = 1;

    private String selectedPhotoUrl;

    private Uri tempImageUri;


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


        recyclerView = findViewById(R.id.accountsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        accountItemList = new ArrayList<>(); // Initialisez la liste ici
        accountAdapter = new AccountAdapter(accountItemList, R.layout.item_account); // Initialisez l'adaptateur
        recyclerView.setAdapter(accountAdapter); // Attachez l'adaptateur

        fetchAndDisplayChildAccounts();



        // Créez une liste d'exemple d'éléments de téléchargement
        List<DownloadItem> downloadItemList = new ArrayList<>();
        downloadItemList.add(new DownloadItem(R.drawable.film1, "Download 1"));
        downloadItemList.add(new DownloadItem(R.drawable.film2, "Download 2"));
        downloadItemList.add(new DownloadItem(R.drawable.film3, "Download 3"));
        downloadItemList.add(new DownloadItem(R.drawable.add_icon, "New Film"));

        // Obtenez la référence du RecyclerView pour la section "My Selection"
        RecyclerView recyclerView2 = findViewById(R.id.accountsRecyclerView2);

        // Créez un adaptateur pour les éléments de téléchargement
        DownloadAdapter downloadAdapter = new DownloadAdapter(downloadItemList, R.layout.item_downloads, this); // this représente le contexte
        recyclerView.setAdapter(downloadAdapter);

        // Configurez le RecyclerView avec un gestionnaire de disposition horizontal
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        // Définissez l'adaptateur sur le RecyclerView
        recyclerView2.setAdapter(downloadAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            tempImageUri = data.getData();
        }
    }


    private void uploadImageToFirebase(Uri imageUri, ChildAccount childAccount) {
        if (imageUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("child_images/" + System.currentTimeMillis() + ".jpg");

            storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    childAccount.setPhotoUrl(imageUrl);  // Mettre à jour l'URL de la photo dans l'objet ChildAccount
                    saveNewChildAccount(childAccount);  // Enregistrer le ChildAccount dans Firebase Database
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(ProfileActivity.this, "Erreur lors du chargement de l'image", Toast.LENGTH_SHORT).show();
            });
        }
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

    private void showEditUserDialog(String currentUserName, String currentUserEmail) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_edit_user);

        EditText editTextName = dialog.findViewById(R.id.editTextFullName);
        EditText editTextEmail = dialog.findViewById(R.id.editTextEmail);
        EditText editTextPassword = dialog.findViewById(R.id.editTextPassword);

        editTextName.setText(currentUserName);
        editTextEmail.setText(currentUserEmail);

        Button buttonSave = dialog.findViewById(R.id.buttonSave);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

        buttonSave.setOnClickListener(view -> {
            String newName = editTextName.getText().toString();
            String newEmail = editTextEmail.getText().toString();
            String newPassword = editTextPassword.getText().toString();

            if (!newEmail.equals(currentUserEmail) || !newPassword.isEmpty()) {
                // Si l'email ou le mot de passe a changé, demandez la re-authentification
                promptReauthentication(newName, newEmail, newPassword);
            } else {
                // Sinon, mettez à jour uniquement le nom
                updateUserDatabaseInfo(FirebaseAuth.getInstance().getCurrentUser().getUid(), newName, currentUserEmail, "");
            }
            dialog.dismiss();
        });

        buttonCancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void promptReauthentication(String newName, String newEmail, String newPassword) {
        new AlertDialog.Builder(this)
                .setTitle("Re-authentication Required")
                .setMessage("To update your email or password, please re-authenticate.")
                .setPositiveButton("Re-authenticate", (dialog, which) -> {
                    redirectToSignIn(newName, newEmail, newPassword);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void redirectToSignIn(String newName, String newEmail, String newPassword) {
        Intent signInIntent = new Intent(this, SinginActivity.class);
        signInIntent.putExtra("newName", newName);
        signInIntent.putExtra("newEmail", newEmail);
        signInIntent.putExtra("newPassword", newPassword);
        startActivity(signInIntent);
        finish();
    }


    private void updateUserDatabaseInfo(String userId, String updatedName, String updatedEmail, String updatedPassword) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userReference.child("fullName").setValue(updatedName);
        userReference.child("email").setValue(updatedEmail);

        if (!updatedPassword.isEmpty()) {
            // Update password
            FirebaseAuth.getInstance().getCurrentUser().updatePassword(updatedPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Profile and password updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    handleFirebaseException(task.getException(), "Failed to update password");
                }
            });
        } else {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFirebaseException(Exception exception, String defaultMsg) {
        if (exception instanceof FirebaseAuthRecentLoginRequiredException) {
            // User needs to re-authenticate
            promptReauthentication();
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            // Invalid email or password
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show();
        } else {
            // Other error
            Toast.makeText(this, defaultMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private void promptReauthentication() {
        new AlertDialog.Builder(this)
                .setTitle("Re-authentication Required")
                .setMessage("To update your email or password, please re-authenticate.")
                .setPositiveButton("Re-authenticate", (dialog, which) -> redirectToSignIn())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void redirectToSignIn() {
        // Intent pour rediriger vers la page de connexion
        Intent signInIntent = new Intent(this, SinginActivity.class);
        startActivity(signInIntent);
        finish();
    }


    private void fetchAndDisplayChildAccounts() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userReference = databaseReference.child(uid).child("childAccounts");

            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    accountItemList.clear(); // Nettoyez la liste avant de l'ajouter

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        ChildAccount child = childSnapshot.getValue(ChildAccount.class);
                        if (child != null) {
                            accountItemList.add(convertToAccountItem(child));
                        }
                    }

                    // Ajoutez "New Account" à la fin de la liste
                    accountItemList.add(new AccountItem(R.drawable.add_icon, "New Account", null));

                    // Avertissez l'adaptateur que les données ont changé
                    accountAdapter.notifyDataSetChanged();
                    // Configurez le gestionnaire de clics pour détecter le clic sur "New Account"
                    accountAdapter.setOnItemClickListener(new AccountAdapter.OnItemClickListener() {
                        @Override
                        public void onNewAccountClick() {
                            showAddChildDialog();
                        }

                        @Override
                        public void onChildAccountClick(int position, AccountItem accountItem) {
                            showAccountOptionsDialog(position, accountItem);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Gérer l'erreur
                }
            });
        }
    }


    private AccountItem convertToAccountItem(ChildAccount childAccount) {
        if (childAccount.getPhotoUrl() != null && !childAccount.getPhotoUrl().isEmpty()) {
            // Si une URL de photo est disponible, utilisez-la
            return new AccountItem(0, childAccount.getChildName(), childAccount.getPhotoUrl());
        } else {
            // Sinon, utilisez l'avatar par défaut
            return new AccountItem(childAccount.getAvatarResourceId(), childAccount.getChildName(), null);
        }
    }


    private void showAddChildDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_child);

        // Configuration des paramètres de la fenêtre du dialogue
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        int dialogWidth = (int)(getResources().getDisplayMetrics().widthPixels * 0.85); // 85% de la largeur de l'écran
        params.width = dialogWidth;
        dialog.getWindow().setAttributes(params);

        EditText editTextChildName = dialog.findViewById(R.id.editTextChildName);
        ImageView avatarFemale = dialog.findViewById(R.id.avatarFemale);
        ImageView avatarMale = dialog.findViewById(R.id.avatarMale);
        Button buttonSelectFromGallery = dialog.findViewById(R.id.buttonSelectFromGallery);
        Button buttonSave = dialog.findViewById(R.id.buttonSave);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

        final int[] selectedAvatarResource = {R.drawable.default_avatar}; // ID de ressource pour l'avatar par défaut
        final String[] selectedPhotoUrl = {null}; // URL de la photo sélectionnée

        // Gestion des clics sur les avatars
        avatarFemale.setOnClickListener(v -> {
            selectedAvatarResource[0] = R.drawable.child_female;
            selectedPhotoUrl[0] = null;
        });

        avatarMale.setOnClickListener(v -> {
            selectedAvatarResource[0] = R.drawable.child_male;
            selectedPhotoUrl[0] = null;
        });

        // Sélection de l'image depuis la galerie
        buttonSelectFromGallery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        });

        // Enregistrer un nouveau compte enfant
        buttonSave.setOnClickListener(v -> {
            String childName = editTextChildName.getText().toString().trim();
            if (!childName.isEmpty()) {
                ChildAccount newChild = new ChildAccount(childName, selectedAvatarResource[0], null);
                if (tempImageUri != null) {
                    uploadImageToFirebase(tempImageUri, newChild); // Utilisez tempImageUri ici
                } else {
                    saveNewChildAccount(newChild);
                }
                dialog.dismiss();
            } else {
                editTextChildName.setError("Le nom de l'enfant est requis !");
            }
        });



        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    private void saveNewChildAccount(ChildAccount childAccount) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("childAccounts");

            // Ajouter le nouveau compte enfant à Firebase
            userReference.push().setValue(childAccount)
                    .addOnSuccessListener(aVoid -> Toast.makeText(ProfileActivity.this, "Nouveau compte enfant ajouté", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show());

            // Rafraîchir la liste des comptes enfants
            fetchAndDisplayChildAccounts();
        }
    }



    private void showAccountOptionsDialog(int position, AccountItem accountItem) {
        Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(accountItem.getChildName());

        // Les options avec des icônes
        CharSequence[] options = new CharSequence[] {
                Html.fromHtml("<font color='#FF0000'>Delete this Account</font>"),
                "Switch to this Account"
        };

        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Suppression du compte
                confirmDeletion(position, accountItem);
            } else if (which == 1) {
                // Changement de compte
                // Implémentez ici la logique pour changer de compte
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void confirmDeletion(int position, AccountItem accountItem) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmer la suppression")
                .setMessage("Êtes-vous sûr de vouloir supprimer ce compte ?")
                .setPositiveButton("Supprimer", (dialogInterface, i) -> deleteChildAccount(position, accountItem))
                .setNegativeButton("Annuler", null)
                .show();



    }

    private void deleteChildAccount(int position, AccountItem accountItem) {
        DatabaseReference childRef = databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("childAccounts");
        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    ChildAccount child = childSnapshot.getValue(ChildAccount.class);
                    if (child != null && child.getChildName().equals(accountItem.getChildName())) {
                        String key = childSnapshot.getKey();
                        if (child.getPhotoUrl() != null && !child.getPhotoUrl().isEmpty()) {
                            // Supprimez d'abord l'image du stockage Firebase si elle existe
                            StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(child.getPhotoUrl());
                            photoRef.delete().addOnSuccessListener(aVoid -> {
                                // Ensuite, supprimez le compte enfant de la base de données Firebase
                                childRef.child(key).removeValue();
                                accountItemList.remove(position);
                                accountAdapter.notifyItemRemoved(position);
                                Toast.makeText(ProfileActivity.this, "Compte enfant supprimé", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> {
                                Toast.makeText(ProfileActivity.this, "Erreur lors de la suppression de l'image", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            // Il n'y a pas d'image à supprimer, supprimez simplement le compte enfant
                            childRef.child(key).removeValue();
                            accountItemList.remove(position);
                            accountAdapter.notifyItemRemoved(position);
                            Toast.makeText(ProfileActivity.this, "Compte enfant supprimé", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
            }
        });
    }


}