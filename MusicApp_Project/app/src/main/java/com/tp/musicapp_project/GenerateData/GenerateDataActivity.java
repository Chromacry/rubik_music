package com.tp.musicapp_project.GenerateData;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tp.musicapp_project.HomePage;
import com.tp.musicapp_project.LibraryPage;
import com.tp.musicapp_project.R;
import com.tp.musicapp_project.SearchPage;

import java.util.ArrayList;
import java.util.List;


// ###PLEASE DO NOT ADD OR TOUCH ANYTHING HERE!!###
// ###FOR DEVELOPERS ONLY!!! ###
// ### SENSITIVE FEATURE FOR ADDING DATA TO DATABASE!! ###
public class GenerateDataActivity extends AppCompatActivity {
    EditText editSongTitle, editSongArtist, editSongGenre, editSongCategory, editSongFileLink, editSongImageLink;
    Button btnPublish;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("");

    //List to store all the music from firebase database
    List<GenerateSongData> songsData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_data);

        //Init and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);
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
                    startActivity(new Intent(getApplicationContext(), LibraryPage.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });

        //Init database
        databaseReference = FirebaseDatabase.getInstance().getReference("songCollection"); //For Adding Songs

        //getting Views and button
        editSongTitle = (EditText) findViewById(R.id.txt_SongTitle);
        editSongArtist = (EditText) findViewById(R.id.txt_SongArtist);
        editSongGenre = (EditText) findViewById(R.id.txt_SongGenre);
        editSongCategory = (EditText) findViewById(R.id.txt_SongCategory);
        editSongFileLink = (EditText) findViewById(R.id.txt_SongFileLink);
        editSongImageLink = (EditText) findViewById(R.id.txt_SongImageLink);
        btnPublish = (Button) findViewById(R.id.btnPublish);




        //list to store artists
        songsData = new ArrayList<>();

        //button listener
        btnPublish.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { addData(); }});
    }

    private void addData(){
        //getting the values to save
        String songTitle = editSongTitle.getText().toString().trim();
        String songArtist = editSongArtist.getText().toString().trim();
        String songGenre = editSongGenre.getText().toString().trim();
        String songCategory = editSongCategory.getText().toString().trim();
        String songFileLink = editSongFileLink.getText().toString().trim();
        String songImageLink = editSongImageLink.getText().toString().trim();

        //checking if the value is provided
        if (!TextUtils.isEmpty(songTitle) && !TextUtils.isEmpty(songArtist) && !TextUtils.isEmpty(songGenre) && !TextUtils.isEmpty(songCategory) && !TextUtils.isEmpty(songFileLink) && !TextUtils.isEmpty(songImageLink)) {
            //getting a unique id using push().getKey() method
            //create a unique id and we will use it as the Primary Key for our songData
            String id = databaseReference.push().getKey();

            //creating an Artist Object
            GenerateSongData songData = new GenerateSongData(id, songTitle, songArtist, songGenre, songCategory, songFileLink, songImageLink);

            //Saving the Artist
            databaseReference.child(id).setValue(songData);

            //setting edittext to blank again
            editSongTitle.setText("");
            editSongArtist.setText("");
            editSongGenre.setText("");
            editSongCategory.setText("");
            editSongFileLink.setText("");
            editSongImageLink.setText("");

            //displaying a success toast
            Toast.makeText(this, "SongData added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter all Fields CORRECTLY!!", Toast.LENGTH_LONG).show();
        }
    }




}