package com.example.photobooth.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhotoStripDao {
    @Insert
    void insert(PhotoStrip strip);

    @Query("SELECT * FROM photo_strips ORDER BY createdAt DESC")
    List<PhotoStrip> getAll();
}