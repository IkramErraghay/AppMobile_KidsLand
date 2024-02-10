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

        videoView = findViewById(R.id.videoView);


        // Récupérer l'URL de la vidéo depuis l'intent, si elle est présente
        String videoUrl = getIntent().getStringExtra("video_url");

        setupVoiceRecognition();

        if (videoUrl != null && !videoUrl.isEmpty()) {
            Uri videoUri = Uri.parse(videoUrl);
            setupVideoView(videoUri);
        } else {
            int videoResId = getIntent().getIntExtra("video_res_id", 0);
            if (videoResId != 0) {
                Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResId);
                setupVideoView(videoUri);
            }
        }

        videoView.setOnCompletionListener(mediaPlayer -> {
            // Lancer la reconnaissance vocale à la fin de la vidéo
            startVoiceRecognition();

        });


    }

    private void setupVoiceRecognition() {
        voiceRecognitionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ArrayList<String> matches = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && matches.contains("oui")) {

                            String videoUrl = getIntent().getStringExtra("video_url");
                            Uri videoUri = Uri.parse(videoUrl);
                            setupVideoView(videoUri);
                        }
                    }
                }
        );
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

    private void setupVideoView(Uri videoUri) {
        videoView.setVideoURI(videoUri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.start();
    }
}
