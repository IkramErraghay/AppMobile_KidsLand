package com.example.kidsland;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidsland.Model.Film;
import com.example.kidsland.Model.Game;
import com.example.kidsland.Model.InteractiveStory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CategorieActivity extends AppCompatActivity implements
        FilmAdapter.OnFilmClickListener,
        InteractiveStoryAdapter.OnInteractiveStoryClickListener,
        GameAdapter.OnGameClickListener {

    private RecyclerView recyclerViewFilms, recyclerViewInteractiveStories, recyclerViewGames;
    private FilmAdapter filmAdapter;
    private InteractiveStoryAdapter interactiveStoryAdapter;
    private GameAdapter gameAdapter;
    private List<Film> filmList = new ArrayList<>();
    private List<InteractiveStory> interactiveStoryList = new ArrayList<>();
    private List<Game> gameList = new ArrayList<>();

    private ImageView rightTopImage;

    @Override
    public void onFilmClick(Film film) {
        StorageReference videoRef = FirebaseStorage.getInstance().getReferenceFromUrl(film.getUrlVideo());
        videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Intent intent = new Intent(CategorieActivity.this, VideoPlayerActivity.class);
                intent.putExtra("video_url", downloadUrl.toString());
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Gérez l'erreur ici
                Toast.makeText(CategorieActivity.this, "Erreur lors du chargement de la vidéo", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onInteractiveStoryClick(InteractiveStory interactiveStory) {
        StorageReference videoRef = FirebaseStorage.getInstance().getReferenceFromUrl(interactiveStory.getUrlVideo());
        videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Intent intent = new Intent(CategorieActivity.this, VideoPlayerActivity.class);
                intent.putExtra("video_url", downloadUrl.toString());
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Gérez l'erreur ici
                Toast.makeText(CategorieActivity.this, "Erreur lors du chargement de la vidéo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onGameClick(Game game) {
        StorageReference videoRef = FirebaseStorage.getInstance().getReferenceFromUrl(game.getUrlVideo());
        videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Intent intent = new Intent(CategorieActivity.this, VideoPlayerActivity.class);
                intent.putExtra("video_url", downloadUrl.toString());
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(CategorieActivity.this, "Erreur lors du chargement de la vidéo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie);

        //pour que lorsque on clique sur le profile il nous redirige vers la page du profile
        rightTopImage = findViewById(R.id.rightTopImage);

        rightTopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvrez l'activité ProfileActivity ici
                Intent intent = new Intent(CategorieActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Initialize RecyclerViews
        recyclerViewFilms = findViewById(R.id.recyclerViewFilms);
        recyclerViewInteractiveStories = findViewById(R.id.recyclerViewInteractiveStories);
        recyclerViewGames = findViewById(R.id.recyclerViewGames);

        // Set layout managers
        recyclerViewFilms.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewInteractiveStories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewGames.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize adapters
        filmAdapter = new FilmAdapter(this, filmList, this);
        interactiveStoryAdapter = new InteractiveStoryAdapter(this, interactiveStoryList, this);
        gameAdapter = new GameAdapter(this, gameList, this);

        // Set adapters
        recyclerViewFilms.setAdapter(filmAdapter);
        recyclerViewInteractiveStories.setAdapter(interactiveStoryAdapter);
        recyclerViewGames.setAdapter(gameAdapter);


        // Fetch data
        fetchFilms();
        fetchInteractiveStories();
        fetchGames();
    }

    private void fetchFilms() {
        FirebaseDatabase.getInstance().getReference("films")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        filmList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Film film = snapshot.getValue(Film.class);
                            filmList.add(film);
                        }
                        filmAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void fetchInteractiveStories() {
        FirebaseDatabase.getInstance().getReference("interactive_stories")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        interactiveStoryList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            InteractiveStory interactiveStory = snapshot.getValue(InteractiveStory.class);
                            interactiveStoryList.add(interactiveStory);
                        }
                        interactiveStoryAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Log error or show error message
                    }
                });
    }

    private void fetchGames() {
        FirebaseDatabase.getInstance().getReference("games")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        gameList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Game game = snapshot.getValue(Game.class);
                            gameList.add(game);
                        }
                        gameAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Log error or show error message
                    }
                });
    }

}
