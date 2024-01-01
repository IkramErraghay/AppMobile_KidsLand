package com.example.kidsland.Model;

public class ChildAccount {
    private String childName;
    private int avatarResourceId; // Ressource de l'avatar ou 0 si aucune ressource définie
    private String photoUrl; // URL de la photo du fils ou null si aucune photo définie

    public ChildAccount() {
        // Constructeur vide
    }

    public ChildAccount(String childName, int avatarResourceId, String photoUrl) {
        this.childName= childName;
        this.avatarResourceId=avatarResourceId;
        this.photoUrl=photoUrl;
    }


    public String getChildName(){
        return childName;
    }

    public int getAvatarResourceId(){
        return avatarResourceId;
    }



    public void setChildName(String childName) {
        this.childName=childName;

    }

    public void setAvatarResourceId(Integer avatarResourceId) {
        this.avatarResourceId=avatarResourceId;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}