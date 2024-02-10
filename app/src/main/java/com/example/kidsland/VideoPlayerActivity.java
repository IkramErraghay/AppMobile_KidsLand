package com.example.kidsland;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> voiceRecognitionLauncher;
    private VideoView videoView;
   

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        voiceRecognitionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ArrayList<String> matches = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && matches.contains("oui")) {
                            // Réagir à la réponse "oui"
                            int newVideoResId = R.raw.video1; // Changez video2 par le nom de votre vidéo de remplacement
                            Uri newVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + newVideoResId);
                            setupVideoView(newVideoUri);
                        }
                    }
                }
        );

        videoView = findViewById(R.id.videoView);


        int videoResId = getIntent().getIntExtra("video_res_id", 0);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResId);
        setupVideoView(videoUri);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // Lancer la reconnaissance vocale à la fin de la vidéo
                startVoiceRecognition();
            }
        });

        // Configurez l'écouteur de clic pour le bouton 1 pour une action spécifique si nécessaire...
    }

    private void setupVideoView(Uri videoUri) {
        videoView.setVideoURI(videoUri);

        MediaController mediaController = new MediaController(VideoPlayerActivity.this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.start();
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Parlez maintenant...");
        try {
            voiceRecognitionLauncher.launch(intent);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Votre appareil ne supporte pas la reconnaissance vocale", Toast.LENGTH_SHORT).show();
        }
    }
}
