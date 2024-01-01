package com.example.kidsland;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private List<AccountItem> accountItemList;
    private int itemLayout; // Ajout de la variable pour stocker le layout
    private OnItemClickListener onItemClickListener; // Ajout de l'interface pour gérer les clics

    // Interface pour gérer les clics
    public interface OnItemClickListener {
        void onNewAccountClick();
        void onChildAccountClick(int position, AccountItem accountItem);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public AccountAdapter(List<AccountItem> accountItemList, int itemLayout) {
        this.accountItemList = accountItemList;
        this.itemLayout = itemLayout;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        AccountItem accountItem = accountItemList.get(position);
        holder.childNameTextView.setText(accountItem.getChildName());

        if (accountItem.getPhotoUrl() != null && !accountItem.getPhotoUrl().isEmpty()) {
            // Utiliser Glide ou Picasso pour charger l'image
            Glide.with(holder.itemView.getContext())
                    .load(accountItem.getPhotoUrl())
                    .into(holder.avatarImageView);
        } else {
            // Utiliser l'avatar par défaut
            holder.avatarImageView.setImageResource(accountItem.getAvatarResId());
        }

        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                if (position == accountItemList.size() - 1) {
                    onItemClickListener.onNewAccountClick();
                } else {
                    onItemClickListener.onChildAccountClick(position, accountItem);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return accountItemList.size();
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView childNameTextView;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            childNameTextView = itemView.findViewById(R.id.childNameTextView);
        }
    }
}
