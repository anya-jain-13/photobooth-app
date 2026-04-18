package com.example.photobooth.model;

public class BoothLayout {
    private final String name;
    private final int photoCount;
    private final String sizeLabel;

    public BoothLayout(String name, int photoCount, String sizeLabel) {
        this.name = name;
        this.photoCount = photoCount;
        this.sizeLabel = sizeLabel;
    }

    public String getName() {
        return name;
    }

    public int getPhotoCount() {
        return photoCount;
    }

    public String getSizeLabel() {
        return sizeLabel;
    }
}