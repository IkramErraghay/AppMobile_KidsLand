package com.example.kidsland;

public class AccountItem {
    private int avatarResId;
    private String childName;

    public AccountItem(int avatarResId, String childName) {
        this.avatarResId = avatarResId;
        this.childName = childName;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public String getChildName() {
        return childName;
    }
}

