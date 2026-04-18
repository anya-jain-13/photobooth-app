package com.example.photobooth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photobooth.model.BoothLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.layoutRecycler);
        Button btnGallery = findViewById(R.id.btnGallery);

        List<BoothLayout> layouts = new ArrayList<>();
        layouts.add(new BoothLayout("Layout A", 3, "3 Photos Strip"));
        layouts.add(new BoothLayout("Layout B", 4, "4 Photos Strip"));
        layouts.add(new BoothLayout("Layout C", 2, "2 Photos Strip"));
        layouts.add(new BoothLayout("Layout D", 6, "6 Photos Strip"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LayoutAdapter(this, layouts));

        btnGallery.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, GalleryActivity.class)));
    }
}