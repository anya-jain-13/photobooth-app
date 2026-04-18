package com.example.photobooth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photobooth.data.AppDatabase;
import com.example.photobooth.data.PhotoStrip;

import java.util.List;
import java.util.concurrent.Executors;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView stripRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        stripRecycler = findViewById(R.id.stripRecycler);
        stripRecycler.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    private void loadData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<PhotoStrip> strips = AppDatabase.getInstance(this).photoStripDao().getAll();
            runOnUiThread(() -> stripRecycler.setAdapter(new StripAdapter(this, strips)));
        });
    }
}