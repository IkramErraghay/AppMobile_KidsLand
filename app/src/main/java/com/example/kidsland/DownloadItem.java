package com.example.kidsland;

// DownloadItem.java
public class DownloadItem {
    private int imageResource;
    private String itemName;

    public DownloadItem(int imageResource, String itemName) {
        this.imageResource = imageResource;
        this.itemName = itemName;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getItemName() {
        return itemName;
    }
}
