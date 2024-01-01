package com.example.kidsland;

public class AccountItem {
    private int avatarResId;
    private String childName;

    private String photoUrl;
    public AccountItem(int avatarResId, String childName, String photoUrl) {
        this.avatarResId = avatarResId;
        this.childName = childName;
        this.photoUrl = photoUrl;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public String getChildName() {
        return childName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}

