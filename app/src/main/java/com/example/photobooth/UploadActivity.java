package com.example.photobooth;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.photobooth.data.AppDatabase;
import com.example.photobooth.data.PhotoStrip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class UploadActivity extends AppCompatActivity {

    private final List<Bitmap> selectedImages = new ArrayList<>();
    private int requiredPhotos;
    private String layoutName;

    private TextView txtSelectedCount;
    private LinearLayout imageContainer;

    // 📸 IMAGE PICKER
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (inputStream != null) inputStream.close();

                        if (bitmap != null) {
                            selectedImages.add(bitmap);

                            // ✅ UPDATE COUNT
                            txtSelectedCount.setText("Selected: " + selectedImages.size() + "/" + requiredPhotos);

                            // ✅ SHOW IMAGE PREVIEW
                            ImageView imageView = new ImageView(this);
                            imageView.setImageBitmap(bitmap);
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    400
                            ));
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            imageContainer.addView(imageView);

                        } else {
                            Toast.makeText(this, "Could not load image", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Error selecting image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        requiredPhotos = getIntent().getIntExtra("photoCount", 4);
        layoutName = getIntent().getStringExtra("layoutName");

        TextView txtTitle = findViewById(R.id.txtUploadTitle);
        txtSelectedCount = findViewById(R.id.txtSelectedCount);
        Button btnUpload = findViewById(R.id.btnUpload);
        Button btnGenerate = findViewById(R.id.btnGenerate);
        imageContainer = findViewById(R.id.imageContainer);

        txtTitle.setText(layoutName + " • Upload " + requiredPhotos + " photos");
        txtSelectedCount.setText("Selected: 0/" + requiredPhotos);

        // 📥 UPLOAD BUTTON
        btnUpload.setOnClickListener(v -> {
            if (selectedImages.size() >= requiredPhotos) {
                Toast.makeText(this, "Enough images selected", Toast.LENGTH_SHORT).show();
                return;
            }

            pickMedia.launch(
                    new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build()
            );
        });

        // 🎯 GENERATE STRIP
        btnGenerate.setOnClickListener(v -> {
            if (selectedImages.size() < requiredPhotos) {
                Toast.makeText(this,
                        "Upload " + requiredPhotos + " images first",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String path = createPhotoStrip(selectedImages);
            saveStripToDatabase(path);

            Toast.makeText(this, "Photo strip created!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, GalleryActivity.class));
            finish();
        });
    }

    // 🎨 CREATE PHOTO STRIP
    private String createPhotoStrip(List<Bitmap> images) {
        try {
            int width = 720;
            int photoHeight = 420;
            int spacing = 20;

            Bitmap strip = Bitmap.createBitmap(width,
                    (photoHeight + spacing) * images.size(),
                    Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(strip);
            canvas.drawColor(Color.WHITE);

            int y = 0;

            for (Bitmap bmp : images) {
                Bitmap scaled = Bitmap.createScaledBitmap(bmp, width, photoHeight, true);
                canvas.drawBitmap(scaled, 0, y, null);
                y += photoHeight + spacing;
            }

            File file = new File(getExternalFilesDir(null),
                    "strip_" + System.currentTimeMillis() + ".jpg");

            FileOutputStream out = new FileOutputStream(file);
            strip.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();

            saveToGallery(file);

            return file.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 💾 SAVE TO GALLERY
    private void saveToGallery(File file) {
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, file.getName());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PhotoBooth");

            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (uri != null) {
                try (java.io.OutputStream out = getContentResolver().openOutputStream(uri);
                     java.io.FileInputStream in = new java.io.FileInputStream(file)) {

                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🗄 SAVE TO DATABASE
    private void saveStripToDatabase(String path) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            db.photoStripDao().insert(new PhotoStrip(layoutName, path, System.currentTimeMillis()));
        });
    }
}

