package com.tp.musicapp_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tp.musicapp_project.GenerateData.MusicCollection;
import com.tp.musicapp_project.GenerateData.SongData;
import com.tp.musicapp_project.GenerateData.SongPlaylistData;
import com.tp.musicapp_project.OtherClasses.HelperClasses.AdapterRecommanded;
import com.tp.musicapp_project.OtherClasses.HelperClasses.AdapterSearch;
import com.tp.musicapp_project.OtherClasses.HelperClasses.AdapterSong;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaylistPage extends AppCompatActivity {
    MusicCollection musicCollection = new MusicCollection();
    private String playlistID, playlistName, playlistImage;

    private List<Object> playlistSongdata;
    private ArrayList<SongData> playlistSongs = new ArrayList<SongData>();

    RecyclerView playlistrecycler;
    RecyclerView.Adapter playlistpageAdapter;
    TextView playlistTitle;
    ImageView playlistImg, btnPlaylistPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_page);

        //Init stuff
        playlistrecycler = findViewById(R.id.playlistresults_recycler);
        playlistTitle = findViewById(R.id.txtPlaylistName);
        playlistImg = findViewById(R.id.playlistImg);
        btnPlaylistPlay = findViewById(R.id.btnPlaylistPlay);


        //Get Data from activity
        Bundle songData = this.getIntent().getExtras();
        playlistID = songData.getString("songData");

        displayDetails();
        objRecycler();
    }

    public void displayDetails(){
        SongPlaylistData songPlaylistData = musicCollection.getPlaylistDetails(playlistID);

        playlistName = songPlaylistData.getPlaylistTitle();
        playlistImage = songPlaylistData.getSongImageLink();

        //Set stuff
        playlistTitle.setText(playlistName);
        Picasso.get().load(playlistImage).into(playlistImg);

        //For Song Playlist Data
        playlistSongdata = (List<Object>) songPlaylistData.getSongsID();
//        Log.d("data",playlistSongdata.get(0).toString());
    }

    public void objRecycler(){
        playlistrecycler.setHasFixedSize(true);
        playlistrecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //Add song into arraylist after query in arraylist by stringID
        for (int i = 0; i < playlistSongdata.size(); i++) {
            int tempIndex = musicCollection.searchSongById(playlistSongdata.get(i).toString(), MainActivity.songCollection);
            SongData songdata = musicCollection.getCurrentSong(tempIndex, MainActivity.songCollection);
            playlistSongs.add(songdata);
        }


        playlistpageAdapter = new AdapterSearch(playlistSongs,this);
        playlistrecycler.setAdapter(playlistpageAdapter);

    }

    public void onClickPlaylistPlay(View view){
        Intent intent = new Intent(this, PlaySongActivity.class);
        intent.putExtra("playlistID", playlistID);
        startActivity(intent);
    }
}