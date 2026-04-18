package com.example.photobooth.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "photo_strips")
public class PhotoStrip {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String layoutName;
    public String imagePath;
    public long createdAt;

    public PhotoStrip(String layoutName, String imagePath, long createdAt) {
        this.layoutName = layoutName;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
    }
}