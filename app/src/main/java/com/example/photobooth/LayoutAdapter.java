package com.example.photobooth;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photobooth.model.BoothLayout;

import java.util.List;

public class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.LayoutViewHolder> {

    private final Context context;
    private final List<BoothLayout> layouts;

    public LayoutAdapter(Context context, List<BoothLayout> layouts) {
        this.context = context;
        this.layouts = layouts;
    }

    @NonNull
    @Override
    public LayoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new LayoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LayoutViewHolder holder, int position) {
        BoothLayout layout = layouts.get(position);

        holder.txtLayoutName.setText(layout.getName());
        holder.txtLayoutInfo.setText(layout.getPhotoCount() + " photos • " + layout.getSizeLabel());

        holder.btnStartLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, UploadActivity.class);
            intent.putExtra("layoutName", layout.getName());
            intent.putExtra("photoCount", layout.getPhotoCount());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return layouts.size();
    }

    static class LayoutViewHolder extends RecyclerView.ViewHolder {
        TextView txtLayoutName, txtLayoutInfo;
        Button btnStartLayout;

        public LayoutViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLayoutName = itemView.findViewById(R.id.txtLayoutName);
            txtLayoutInfo = itemView.findViewById(R.id.txtLayoutInfo);
            btnStartLayout = itemView.findViewById(R.id.btnStartLayout);
        }
    }
}