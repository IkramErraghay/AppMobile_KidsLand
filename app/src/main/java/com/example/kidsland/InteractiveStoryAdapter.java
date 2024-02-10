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
import com.example.kidsland.Model.InteractiveStory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class InteractiveStoryAdapter extends RecyclerView.Adapter<InteractiveStoryAdapter.InteractiveStoryViewHolder> {

    private Context context;
    private List<InteractiveStory> interactiveStories;

    private OnInteractiveStoryClickListener listener;


    public InteractiveStoryAdapter(Context context, List<InteractiveStory> interactiveStories, OnInteractiveStoryClickListener listener) {
        this.context = context;
        this.interactiveStories = interactiveStories;
        this.listener= listener;
    }

    @NonNull
    @Override
    public InteractiveStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_interactive_story, parent, false);
        return new InteractiveStoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InteractiveStoryViewHolder holder, int position) {
        InteractiveStory story = interactiveStories.get(position);
        holder.textViewTitle.setText(story.getTitre());

        holder.bind(story, listener);
        // Get the Firebase Storage reference from the URL saved in the film object
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(story.getUrlImage());

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
        return interactiveStories.size();
    }

    public static class InteractiveStoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCover;
        TextView textViewTitle;

        public InteractiveStoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.imageViewCover);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }

        public void bind(InteractiveStory story, OnInteractiveStoryClickListener listener) {
            textViewTitle.setText(story.getTitre());
            Glide.with(itemView.getContext()).load(story.getUrlImage()).into(imageViewCover);
            itemView.setOnClickListener(view -> listener.onInteractiveStoryClick(story));
        }
    }

    public interface OnInteractiveStoryClickListener {
        void onInteractiveStoryClick(InteractiveStory interactiveStory);
    }

}
