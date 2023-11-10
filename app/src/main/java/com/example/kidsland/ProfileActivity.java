package com.example.kidsland;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
}
