package com.example.kidsland;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = findViewById(R.id.videoView);

        // Récupérez l'ID de la ressource de la vidéo à partir de l'Intent
        int videoResId = getIntent().getIntExtra("video_res_id", 0);

        // Construisez l'URI de la vidéo à partir de la ressource
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResId);

        // Configurez le VideoView pour lire la vidéo
        videoView.setVideoURI(videoUri);

        // Ajoutez des contrôles pour la lecture de la vidéo
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        // Commencez la lecture de la vidéo
        videoView.start();
    }
}