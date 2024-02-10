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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {

    private List<Film> filmList;
    private Context context;

    public interface OnFilmClickListener {
        void onFilmClick(Film film);
    }

    private OnFilmClickListener listener;

    public FilmAdapter(Context context, List<Film> filmList, OnFilmClickListener listener) {
        this.context = context;
        this.filmList = filmList;
        this.listener = listener;
    }



    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_film, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film film = filmList.get(position);
        holder.textViewTitle.setText(film.getTitre());
        holder.bind(film, listener);
        // Get the Firebase Storage reference from the URL saved in the film object
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(film.getUrlImage());

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
        return filmList.size();
    }

    static class FilmViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCover; // Correct usage
        TextView textViewTitle; // Correct usage

        public FilmViewHolder(View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.imageViewCover);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }
        public void bind(Film film, OnFilmClickListener listener) {
            textViewTitle.setText(film.getTitre());
            Glide.with(itemView.getContext()).load(film.getUrlImage()).into(imageViewCover);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onFilmClick(film);
                }
            });
        }

    }



}
