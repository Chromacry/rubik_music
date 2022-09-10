package com.tp.musicapp_project.OtherClasses.HelperClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tp.musicapp_project.GenerateData.SongData;
import com.tp.musicapp_project.HomePage;
import com.tp.musicapp_project.LibraryPage;
import com.tp.musicapp_project.R;

import java.util.ArrayList;

public class AdapterSong extends RecyclerView.Adapter<AdapterSong.PhoneViewHold>  {
    ArrayList<SongData> songs;

    @NonNull
    @Override
    public PhoneViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songbartemplate, parent, false);
        return new PhoneViewHold(view);

    }

    //Constructor
    public AdapterSong(ArrayList<SongData> songs) {
        this.songs = songs;
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneViewHold holder, int position) {
        SongData Song = songs.get(position);

        Picasso.get().load(Song.getSongImageLink()).into(holder.image);
        holder.btnfavRemove.setImageResource(R.drawable.addfavtick);

        holder.title.setText(Song.getSongTitle());
        holder.artist.setText(Song.getSongArtist());
        holder.image.setContentDescription(Song.getId());
        holder.image.setId(position);
        holder.btnfavRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                LibraryPage.favList.remove(pos);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }


    public class PhoneViewHold extends RecyclerView.ViewHolder {
        ImageView image, btnfavRemove;
        TextView title, artist;
        RelativeLayout relativeLayout;
        public PhoneViewHold(@NonNull View itemView) {
            super(itemView);
            //hooks

            image = itemView.findViewById(R.id.img_Song);
            title = itemView.findViewById(R.id.txt_songTitle);
            artist = itemView.findViewById(R.id.txt_songArtist);
            btnfavRemove = itemView.findViewById(R.id.btnfavRemove);
        }
    }
}
