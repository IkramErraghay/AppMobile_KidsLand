package com.example.kidsland.Model;

public class Film {
    private String titre; // Correspond au champ "titre" dans Firebase
    private String urlVideo; // Correspond au champ "urlVideo"

    private String yes; // URL for 'oui' response video
    private String no; // URL for 'oui' response video
    private String idk; // URL for 'oui' response video
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

    public String getYes() {
        return yes;
    }
    public String getNo() {
        return no;
    }
    public String getIdk() {
        return idk;
    }
}
