package com.tp.musicapp_project;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tp.musicapp_project.GenerateData.GenerateDataActivity;
import com.tp.musicapp_project.GenerateData.GeneratePlaylistData;
import com.tp.musicapp_project.GenerateData.GeneratePlaylistDataActivity;
import com.tp.musicapp_project.GenerateData.MusicCollection;
import com.tp.musicapp_project.GenerateData.SongData;
import com.tp.musicapp_project.GenerateData.SongPlaylistData;
import com.tp.musicapp_project.OtherClasses.HelperClasses.AdapterMood;
import com.tp.musicapp_project.OtherClasses.HelperClasses.AdapterPlaylist;
import com.tp.musicapp_project.OtherClasses.HelperClasses.AdapterPopular;
import com.tp.musicapp_project.OtherClasses.HelperClasses.AdapterRecommanded;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity implements AdapterPlaylist.ListItemClickListener, AdapterRecommanded.ListItemClickListener, AdapterPopular.ListItemClickListener, AdapterMood.ListItemClickListener {
    private FirebaseAuth mAuth;
    RecyclerView playlistRecycler, recommandedRecycler, popularRecycler, moodRecycler;
    RecyclerView.Adapter playlistadapter, recommandedadapter, popularadapter, moodadapter;

    DatabaseReference dbSongData = FirebaseDatabase.getInstance().getReference("songCollection");
    DatabaseReference dbSongPlaylistData = FirebaseDatabase.getInstance().getReference("playlistCollection");
    public ArrayList<SongData> recommandedlocations, popularlocations, moodlocations;
    static ArrayList<SongPlaylistData> playlistlocations;

    // Song and playlist Data
    static public ArrayList<SongData> songCollection = new ArrayList<SongData>();
    static public ArrayList<SongPlaylistData> playlistCollection = new ArrayList<SongPlaylistData>();;
    getRecycleDataThread getRecycleDataThread = new getRecycleDataThread();
    getDataThread getDataThread = new getDataThread();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mAuth = FirebaseAuth.getInstance();
        //Generate Grid Playlist and Songs
        playlistRecycler = findViewById(R.id.playlist_recycler);
        recommandedRecycler = findViewById(R.id.recommanded_recycler);
        popularRecycler = findViewById(R.id.popular_recycler);
        moodRecycler = findViewById(R.id.mood_recycler);
        objRecycler();

        //Init and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    return true;
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), SearchPage.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.library:
                    startActivity(new Intent(getApplicationContext(), LibraryPage.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }


    // Settings page
    public void settingsClick(View view){
//        startActivity(new Intent(this, GenerateDataActivity.class)); // For adding Songs Data
        startActivity(new Intent(this, GenerateDataActivity.class)); // For adding Playlist Data
    }


    //Threads
    private class getDataThread extends Thread {
        volatile boolean isRunning = true;

        @Override
        public void run() {
            if (!isRunning) {
                return;
            }
            try {
                getData();
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
    private class getRecycleDataThread extends Thread {
        volatile boolean isRunning = true;

        @Override
        public void run() {
            if (!isRunning) {
                return;
            }
            try {
                getData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void getData() {
            ValueEventListener valueEventListener_recommanded = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    recommandedlocations.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            SongData songData = snapshot.getValue(SongData.class);
                            recommandedlocations.add(songData);
                        }
                        recommandedadapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            ValueEventListener valueEventListener_playlist = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    playlistlocations.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            SongPlaylistData songData = snapshot.getValue(SongPlaylistData.class);
                            playlistlocations.add(songData);
                        }
                        playlistadapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            ValueEventListener valueEventListener_popular = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    popularlocations.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            SongData songData = snapshot.getValue(SongData.class);
                            popularlocations.add(songData);
                        }
                        popularadapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            ValueEventListener valueEventListener_mood = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    moodlocations.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            SongData songData = snapshot.getValue(SongData.class);
                            moodlocations.add(songData);
                        }
                        moodadapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

            // Grid Data
            //Recommanded Data
            Query queryRecommanded = dbSongData.orderByChild("songCategory").equalTo("Recommanded");
            queryRecommanded.addListenerForSingleValueEvent(valueEventListener_recommanded);
            //GoodMorning Playlist Data
            dbSongPlaylistData = FirebaseDatabase.getInstance().getReference("playlistCollection");
            Query queryGoodMorning = dbSongPlaylistData.orderByChild("songCategory").equalTo("GoodMorning");
            queryGoodMorning.addListenerForSingleValueEvent(valueEventListener_playlist);
            // Set Popular Data
            dbSongData = FirebaseDatabase.getInstance().getReference("songCollection");
            Query queryPopular = dbSongData.orderByChild("songCategory").equalTo("Popular");
            queryPopular.addListenerForSingleValueEvent(valueEventListener_popular);
//        dbSongData.addListenerForSingleValueEvent(valueEventListener_popular); //grab everything in Database

            //Mood Data
            dbSongData = FirebaseDatabase.getInstance().getReference("songCollection");
            Query queryMood = dbSongData.orderByChild("songCategory").equalTo("Mood");
            queryMood.addListenerForSingleValueEvent(valueEventListener_mood);
        }
    }
    //Grid Generator
    private void objRecycler() {
        //Recyclers
        // Recommanded Recycler
        recommandedRecycler.setHasFixedSize(true);
        recommandedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //Goodmorning Playlist Recycler
        playlistRecycler.setHasFixedSize(true);
        playlistRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //Popular Recycler
        popularRecycler.setHasFixedSize(true);
        popularRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //Mood Recycler
        moodRecycler.setHasFixedSize(true);
        moodRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Data Creator
        recommandedlocations = new ArrayList<>();
        playlistlocations = new ArrayList<>();
        popularlocations = new ArrayList<>();
        moodlocations = new ArrayList<>();

        //ValueEventListener for Firebase database
        // For RecycleView Data
        getRecycleDataThread.isRunning = true;
        getRecycleDataThread.start();
        // For Song Data
        getDataThread.isRunning = true;
        getDataThread.start();

        //Adapters
        //Recommanded Adapter
        recommandedadapter = new AdapterRecommanded(recommandedlocations,this);
        recommandedRecycler.setAdapter(recommandedadapter);
        //playlist Adapter
        playlistadapter = new AdapterPlaylist(playlistlocations,this);
        playlistRecycler.setAdapter(playlistadapter);
        //Popular Adapter
        popularadapter = new AdapterPopular(popularlocations,this);
        popularRecycler.setAdapter(popularadapter);
//      //Mood Adapter
        moodadapter = new AdapterMood(moodlocations,this);
        moodRecycler.setAdapter(moodadapter);
    }

    @Override
    public void onpopularListClick(int clickedItemIndex) {
        SongData songData = popularlocations.get(clickedItemIndex);
        switch(clickedItemIndex){
            case 0:
            case 1:
            case 2:
            case 3:
                Log.d("data", songData.getSongTitle());
                Intent intent = new Intent(this, PlaySongActivity.class);
                intent.putExtra("songData", songData.getId());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onmoodListClick(int clickedItemIndex) {
        SongData songData = moodlocations.get(clickedItemIndex);
        switch(clickedItemIndex){
            case 0:
            case 1:
            case 2:
            case 3:
                Log.d("data", songData.getSongTitle());
                Intent intent = new Intent(this, PlaySongActivity.class);
                intent.putExtra("songData", songData.getId());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onplaylistListClick(int clickedItemIndex) {
        SongPlaylistData songData = playlistlocations.get(clickedItemIndex);
        switch(clickedItemIndex){
            case 0:
            case 1:
            case 2:
            case 3:
                List<Object> testList = (List<Object>) songData.getSongsID();
//                Log.d("data", testList.get(0).toString());
                Intent intent = new Intent(this, PlaylistPage.class);
                intent.putExtra("songData", songData.getId());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onrecommandedListClick(int clickedItemIndex) {
        SongData songData = recommandedlocations.get(clickedItemIndex);
        switch (clickedItemIndex) {
            case 0:
            case 1:
            case 2:
            case 3:
                Log.d("data", songData.getSongTitle());
                Intent intent = new Intent(this, PlaySongActivity.class);
                intent.putExtra("songData", songData.getId());
                startActivity(intent);
                break;
        }
    }
}