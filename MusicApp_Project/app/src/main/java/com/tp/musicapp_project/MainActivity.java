package com.tp.musicapp_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.tp.musicapp_project.GenerateData.SongData;
import com.tp.musicapp_project.GenerateData.SongPlaylistData;
import com.tp.musicapp_project.OtherClasses.ProgressAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    DatabaseReference dbSongData = FirebaseDatabase.getInstance().getReference("songCollection");
    DatabaseReference dbSongPlaylistData = FirebaseDatabase.getInstance().getReference("playlistCollection");
    static public ArrayList<SongData> songCollection = new ArrayList<SongData>();
    static public ArrayList<SongPlaylistData> playlistCollection = new ArrayList<SongPlaylistData>();;
    getDataThread getDataThread = new getDataThread();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setScaleY(2f);

        //progressbar to change activity
        progressAnimation();


        // Runs getData in thread
        getDataThread.isRunning = true;
        getDataThread.start();
    }

    private void progressAnimation() {

        ProgressAnimation animation = new ProgressAnimation(this, progressBar, 0f, 100f);
        animation.setDuration(4000);
        progressBar.setAnimation(animation);
    }

    private class getDataThread extends Thread {
        volatile boolean isRunning = true;

        @Override
        public void run() {
            if (!isRunning) {
                return;
            }
            try {
                getData();
                Log.d("work", "Song: " + songCollection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void getData() {
            ValueEventListener valueEventListener_song = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    songCollection.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            SongData songData = snapshot.getValue(SongData.class);
                            songCollection.add(songData);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            dbSongData.addListenerForSingleValueEvent(valueEventListener_song); //Get everything from songs
            ValueEventListener valueEventListener_playlist = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        playlistCollection.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                SongPlaylistData songData =snapshot.getValue(SongPlaylistData.class);
                                playlistCollection.add(songData);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
            dbSongPlaylistData.addListenerForSingleValueEvent(valueEventListener_playlist); //Get everything from playlist

        }
    }
}
//    Query querySearch = dbSongData.orderByChild("songTitle"); //Search specifically
//        querySearch.addListenerForSingleValueEvent(valueEventListener_song);