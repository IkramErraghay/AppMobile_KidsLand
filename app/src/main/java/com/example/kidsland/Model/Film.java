package com.example.kidsland.Model;

public class Film {
    private String titre; // Correspond au champ "titre" dans Firebase
    private String urlVideo; // Correspond au champ "urlVideo"
    private String urlImage; // Correspond au champ "urlImage"

    public Film() {
        // Constructeur vide nécessaire pour Firebase
    }

    // Getters et Setters
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
