package com.example.photobooth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.photobooth.data.PhotoStrip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StripAdapter extends RecyclerView.Adapter<StripAdapter.StripViewHolder> {

    private final Context context;
    private final List<PhotoStrip> strips;

    public StripAdapter(Context context, List<PhotoStrip> strips) {
        this.context = context;
        this.strips = strips;
    }

    @NonNull
    @Override
    public StripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_strip, parent, false);
        return new StripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StripViewHolder holder, int position) {
        PhotoStrip strip = strips.get(position);

        Glide.with(context)
                .load(strip.imagePath)
                .into(holder.imgStrip);

        String date = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(new Date(strip.createdAt));

        holder.txtStripInfo.setText(strip.layoutName + " • " + date);
    }

    @Override
    public int getItemCount() {
        return strips.size();
    }

    static class StripViewHolder extends RecyclerView.ViewHolder {
        ImageView imgStrip;
        TextView txtStripInfo;

        public StripViewHolder(@NonNull View itemView) {
            super(itemView);
            imgStrip = itemView.findViewById(R.id.imgStrip);
            txtStripInfo = itemView.findViewById(R.id.txtStripInfo);
        }
    }
}