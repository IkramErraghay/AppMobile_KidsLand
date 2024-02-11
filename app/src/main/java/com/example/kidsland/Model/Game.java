package com.example.kidsland.Model;

public class Game {
    private String titre;
    private String urlVideo;
    private String urlImage;

    private String yes; // URL for 'oui' response video
    private String no; // URL for 'oui' response video
    private String idk; // URL for 'oui' response video

    // Default constructor is necessary for Firebase database operations
    public Game() {
    }

    public Game(String title, String urlVideo, String urlImage) {
        this.titre = title;
        this.urlVideo = urlVideo;
        this.urlImage = urlImage;
    }

    // Getters and setters
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