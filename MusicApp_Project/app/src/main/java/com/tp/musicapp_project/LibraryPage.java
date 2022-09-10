package com.tp.musicapp_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tp.musicapp_project.GenerateData.SongData;
import com.tp.musicapp_project.OtherClasses.HelperClasses.AdapterSong;


import java.util.ArrayList;

public class LibraryPage extends AppCompatActivity {
    RecyclerView favList_recycler;
    AdapterSong songAdapter;


    public static ArrayList<SongData> favList = new ArrayList<>();
    Gson gson = new Gson();
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_page);
        favList_recycler = findViewById(R.id.searchresults_recycler);
        favList_recycler.setHasFixedSize(true);
        favList_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        songAdapter = new AdapterSong(favList);
        favList_recycler.setAdapter(songAdapter);
//        sharedPreferences = getSharedPreferences("myPlaylist", MODE_PRIVATE);
//        String albums = sharedPreferences.getString("myPlaylistList", "");
//        if(!albums.equals("")){
//            TypeToken<ArrayList<SongData>> token = new TypeToken<ArrayList<SongData>>(){};
//            Gson gson = new Gson();
//            favList = gson.fromJson(albums, token.getType());
//        }

//        try{
//            songAdapter = new AdapterSong(favList);
//            favList_recycler.setAdapter(songAdapter);
//        }catch(Exception e){
//            e.printStackTrace();
//        }



        //Init and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.library);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), HomePage.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), SearchPage.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.library:
                    return true;
            }
            return false;
        });
    }

    public void gotoSettingsActivity(View view) {
        for (int i = 0; i < favList.size(); i++) {
            Log.d("btnStatus",favList.get(i).getSongTitle());
        }
    }
}