package com.example.kidsland;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {

    private List<DownloadItem> downloadItemList;
    private int itemLayout;
    private Context context; // Ajout du contexte

    // Ajout du constructeur avec le contexte
    public DownloadAdapter(List<DownloadItem> downloadItemList, int itemLayout, Context context) {
        this.downloadItemList = downloadItemList;
        this.itemLayout = itemLayout;
        this.context = context; // Sauvegarde du contexte
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new DownloadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, int position) {
        DownloadItem downloadItem = downloadItemList.get(position);
        holder.downloadImageView.setImageResource(downloadItem.getImageResource());
        holder.downloadNameTextView.setText(downloadItem.getItemName());

        // Gestion du clic sur l'élément "New Film"
        if (downloadItem.getItemName().equals("New Film")) {
            holder.itemView.setOnClickListener(v -> {
                // Démarrage de CategorieActivity
                Intent intent = new Intent(context, CategorieActivity.class);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return downloadItemList.size();
    }

    static class DownloadViewHolder extends RecyclerView.ViewHolder {
        ImageView downloadImageView;
        TextView downloadNameTextView;

        public DownloadViewHolder(@NonNull View itemView) {
            super(itemView);
            downloadImageView = itemView.findViewById(R.id.downloadImageView);
            downloadNameTextView = itemView.findViewById(R.id.downloadNameTextView);
        }
    }
}
