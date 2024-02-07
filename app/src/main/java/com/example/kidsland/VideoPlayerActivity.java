package com.example.kidsland;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private Button button1, button2, button3; // Ajoutez le troisième bouton

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = findViewById(R.id.videoView);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3); // Initialisation du troisième bouton

        // Initialiser les boutons comme invisibles
        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
        button3.setVisibility(View.GONE);

        // Récupérer l'ID de la ressource de la vidéo à partir de l'Intent
        int videoResId = getIntent().getIntExtra("video_res_id", 0);
        // Construire l'URI de la vidéo à partir de la ressource
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResId);

        // Configurer le VideoView pour lire la vidéo
        setupVideoView(videoUri);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // Rendre les boutons visibles à la fin de la vidéo
                button1.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE); // Rendre le troisième bouton visible également
            }
        });

        // Configurer l'écouteur de clic pour le bouton 1 pour charger une nouvelle vidéo
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button1.setVisibility(View.GONE);
                button2.setVisibility(View.GONE);
                button3.setVisibility(View.GONE);
                // ID de la ressource de la nouvelle vidéo à lire
                int newVideoResId = R.raw.video1; // Assurez-vous que video2.mp4 est présent dans /res/raw
                Uri newVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + newVideoResId);
                setupVideoView(newVideoUri);
            }
        });

        // Configurer les écouteurs de clic pour les boutons 2 et 3 si nécessaire...
    }

    private void setupVideoView(Uri videoUri) {
        videoView.setVideoURI(videoUri);

        MediaController mediaController = new MediaController(VideoPlayerActivity.this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.start(); // Commencer la lecture de la vidéo
    }
}
