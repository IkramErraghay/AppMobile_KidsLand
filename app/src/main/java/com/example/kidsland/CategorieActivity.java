package com.example.kidsland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class CategorieActivity extends AppCompatActivity {

    private HorizontalScrollView horizontalScrollView;
    private Button previousButton;
    private Button nextButton;

    private ImageView film1;
    private ImageView film2;
    private ImageView film3;

    private HorizontalScrollView horizontalScrollView1;
    private Button previousButton1;
    private Button nextButton1;

    private ImageView histoire1;
    private ImageView histoire2;
    private ImageView histoire3;


    private ImageView game1;
    private ImageView game2;
    private ImageView game3;
    private HorizontalScrollView horizontalScrollView2;
    private Button previousButton2;
    private Button nextButton2;

    private  ImageView rightTopImage;


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

        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalScrollView.smoothScrollBy(-300, 0); // Ajustez la valeur selon votre besoin
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalScrollView.smoothScrollBy(300, 0); // Ajustez la valeur selon votre besoin
            }
        });

        film1 = findViewById(R.id.Film1);
        film2 = findViewById(R.id.Film2);
        film3 = findViewById(R.id.Film3);



        film1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategorieActivity.this, VideoPlayerActivity.class);
                intent.putExtra("video_res_id", R.raw.video1); // Remplacez "nom_de_votre_video1" par le nom de votre vidéo dans le dossier "raw"
                startActivity(intent);
                // Action à effectuer lorsque film1 est cliqué
                // Par exemple, ouvrir une nouvelle activité ou effectuer une action spécifique
            }
        });

        film2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lorsque film2 est cliqué
                // Par exemple, ouvrir une nouvelle activité ou effectuer une action spécifique
            }
        });

        film3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lorsque film3 est cliqué
                // Par exemple, ouvrir une nouvelle activité ou effectuer une action spécifique
            }
        });

        horizontalScrollView1 = findViewById(R.id.horizontalScrollView1);
        previousButton1 = findViewById(R.id.previousButton1);
        nextButton1 = findViewById(R.id.nextButton1);

        previousButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalScrollView1.smoothScrollBy(-300, 0); // Ajustez la valeur selon votre besoin
            }
        });

        nextButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalScrollView1.smoothScrollBy(300, 0); // Ajustez la valeur selon votre besoin
            }
        });


        histoire1 = findViewById(R.id.histoir1);
        histoire2 = findViewById(R.id.histoir2);
        histoire3 = findViewById(R.id.histoir3);

        histoire1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lorsque histoire1 est cliqué
                // Par exemple, ouvrir une nouvelle activité ou effectuer une action spécifique
            }
        });

        histoire2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lorsque histoire2 est cliqué
                // Par exemple, ouvrir une nouvelle activité ou effectuer une action spécifique
            }
        });

        histoire3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lorsque histoire3 est cliqué
                // Par exemple, ouvrir une nouvelle activité ou effectuer une action spécifique
            }
        });

        game1 = findViewById(R.id.game1);
        game2 = findViewById(R.id.game2);
        game3 = findViewById(R.id.game3);

        horizontalScrollView2 = findViewById(R.id.horizontalScrollView2);
        previousButton2 = findViewById(R.id.previousButton2);
        nextButton2 = findViewById(R.id.nextButton2);

        game1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action to perform when game1 is clicked
            }
        });

        game2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action to perform when game2 is clicked
            }
        });

        game3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action to perform when game3 is clicked
            }
        });

        previousButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalScrollView2.smoothScrollBy(-300, 0);
            }
        });

        nextButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalScrollView2.smoothScrollBy(300, 0);
            }
        });







    }
}