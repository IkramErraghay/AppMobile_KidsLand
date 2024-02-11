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
import android.util.Log;
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
        setupVoiceRecognition();

        // Récupérer l'URL de la vidéo depuis l'intent, si elle est présente
        String videoUrl = getIntent().getStringExtra("video_url");
        if (videoUrl != null && !videoUrl.isEmpty()) {
            Uri videoUri = Uri.parse(videoUrl);
            setupVideoView(videoUri);
        }

        videoView.setOnCompletionListener(mediaPlayer -> {
            // Lancer la reconnaissance vocale à la fin de la vidéo uniquement si ce n'est pas un film
            String categoryType = getIntent().getStringExtra("category_type");
            if (!"film".equals(categoryType)) {
                startVoiceRecognition();
            }
        });
    }

    private void setupVoiceRecognition() {
        voiceRecognitionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ArrayList<String> matches = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null) {
                            if (matches.contains("oui")) {
                                playVideoFromIntentExtra("yes_video_url");
                            } else if (matches.contains("non")) {
                                playVideoFromIntentExtra("no_video_url");
                            } else {
                                playVideoFromIntentExtra("idk_video_url");
                            }
                        }
                    }
                }
        );
    }


    private void playVideoFromIntentExtra(String key) {
        String videoUrl = getIntent().getStringExtra(key);

        // Si l'URL est non vide, jouez la vidéo correspondante
        if (videoUrl != null && !videoUrl.isEmpty()) {
            Uri videoUri = Uri.parse(videoUrl);
            setupVideoView(videoUri);

            // Si la réponse est "non" ou "idk", relancez la vidéo principale après
            if ("no_video_url".equals(key) || "idk_video_url".equals(key)) {
                videoView.setOnCompletionListener(mp -> {
                    // Relancez la vidéo principale
                    String mainVideoUrl = getIntent().getStringExtra("video_url");
                    if (mainVideoUrl != null && !mainVideoUrl.isEmpty()) {
                        videoView.setVideoURI(Uri.parse(mainVideoUrl));
                        videoView.start();
                    }
                });
            }
        } else {
            // Si l'URL est vide, loggez l'erreur ou affichez un Toast
            Toast.makeText(this, "URL pour la clé '" + key + "' est vide ou non disponible.", Toast.LENGTH_LONG).show();
            finish(); // Fermez l'activité si l'URL attendue n'est pas disponible
        }
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

        // Set the OnErrorListener here
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("VideoPlayerActivity", "MediaPlayer Error: what=" + what + " extra=" + extra);
                Toast.makeText(VideoPlayerActivity.this, "Error while playing video", Toast.LENGTH_SHORT).show();
                // Return true if the error has been handled, otherwise false.
                return true;
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }


}
