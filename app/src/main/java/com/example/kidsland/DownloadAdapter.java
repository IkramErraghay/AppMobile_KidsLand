package com.example.kidsland;

// DownloadAdapter.java
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

    public DownloadAdapter(List<DownloadItem> downloadItemList, int itemLayout) {
        this.downloadItemList = downloadItemList;
        this.itemLayout = itemLayout;
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

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position);
            }
        });
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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}

