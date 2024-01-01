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
        DownloadAdapter downloadAdapter = new DownloadAdapter(downloadItemList, R.layout.item_downloads);

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
            // Vous pouvez éventuellement mettre à jour l'interface utilisateur pour montrer l'image sélectionnée
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