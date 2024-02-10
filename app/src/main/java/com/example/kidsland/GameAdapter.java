package com.example.kidsland;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.kidsland.Model.Film;
import com.example.kidsland.Model.Game;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> gameList;
    private Context context;

    private OnGameClickListener listener;



    public GameAdapter(Context context, List<Game> gameList, GameAdapter.OnGameClickListener listener) {
        this.context = context;
        this.gameList = gameList;
        this.listener = listener;;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gameList.get(position);

        holder.bind(game, listener);
        // Get the Firebase Storage reference from the URL saved in the film object
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(game.getUrlImage());

        // Fetch the download URL
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Here you have the download URL, use Glide to load it
                Glide.with(holder.itemView.getContext())
                        .load(uri.toString()) // Use the download URL here
                        .into(holder.imageViewCover);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }


    @Override
    public int getItemCount() {
        return gameList.size();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCover;
        TextView textViewTitle;

        public GameViewHolder(View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.imageViewCover);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }

        public void bind(Game game, GameAdapter.OnGameClickListener listener) {
            textViewTitle.setText(game.getTitre());
            Glide.with(itemView.getContext()).load(game.getUrlImage()).into(imageViewCover);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onGameClick(game);
                }
            });
        }
    }
    public interface OnGameClickListener {
        void onGameClick(Game game);
    }

}
