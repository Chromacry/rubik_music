package com.tp.musicapp_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tp.musicapp_project.GenerateData.SongData;
import com.tp.musicapp_project.OtherClasses.HelperClasses.AdapterRecommanded;
import com.tp.musicapp_project.OtherClasses.HelperClasses.AdapterSearch;

import java.util.ArrayList;
import java.util.Locale;

public class SearchPage extends AppCompatActivity {
    ArrayList<SongData> allsongs;
    DatabaseReference dbSongData;
    RecyclerView searchresultsrecycler;
    RecyclerView.Adapter adaptersearch, adaptersearch2;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        //Init array for data
        allsongs = new ArrayList<SongData>();
        dbSongData = FirebaseDatabase.getInstance().getReference("songCollection");
        searchView = findViewById(R.id.searchBar);
        searchresultsrecycler = findViewById(R.id.searchresults_recycler);

        //Init and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.search);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), HomePage.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.search:
                    return true;
                case R.id.library:
                    startActivity(new Intent(getApplicationContext(), LibraryPage.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });

        //Firebase stuff
        searchresultsrecycler.setHasFixedSize(true);
        searchresultsrecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ValueEventListener valueEventListener_search = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allsongs.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        SongData songData = snapshot.getValue(SongData.class);
                        allsongs.add(songData);
                    }
                    adaptersearch.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        adaptersearch = new AdapterSearch(HomePage.songCollection, this);
        searchresultsrecycler.setAdapter(adaptersearch);

        if (searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }


        // Query for song Title
        Query querySearch = dbSongData.orderByChild("songTitle"); //Search specifically
        querySearch.addListenerForSingleValueEvent(valueEventListener_search);
//        dbSongData.addListenerForSingleValueEvent(valueEventListener_search); //Get everything from songCollection


    }

    private void search(String str){
        ArrayList<SongData> mysongs = new ArrayList<>();
        for(SongData object: allsongs){
            if(object.getSongTitle().toLowerCase().contains(str.toLowerCase())){
                mysongs.add(object);
            }
        }
        adaptersearch2 = new AdapterSearch(mysongs, this);
        searchresultsrecycler.setAdapter(adaptersearch2);
    }
}