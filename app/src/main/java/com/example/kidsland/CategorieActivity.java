package com.example.kidsland;

import androidx.appcompat.app.AppCompatActivity;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie);

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




    }
}