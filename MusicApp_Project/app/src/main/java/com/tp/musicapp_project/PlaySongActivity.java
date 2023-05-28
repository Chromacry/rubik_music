package com.tp.musicapp_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tp.musicapp_project.GenerateData.MusicCollection;
import com.tp.musicapp_project.GenerateData.SongData;
import com.tp.musicapp_project.GenerateData.SongPlaylistData;

import java.util.ArrayList;
import java.util.List;

public class PlaySongActivity extends AppCompatActivity {
    // Datastore and database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("");
    private FirebaseAuth mAuth;
    Gson gson = new Gson();
    SharedPreferences sharedPreferences;

    private int index;
    public int currentIndex;
    private String title, artiste, fileLink, drawableLink, songid, startDuration, endDuration;
    private String favData;
    private String playlistID;
    private MusicCollection musicCollection = new MusicCollection();


    private MediaPlayer player = new MediaPlayer();
    private AudioManager audioManager;
    private ImageView songImg, songPrevImg, songNextImg, btnLoop, btnPlayPause, btnShuffle, btnfavSong;
    TextView titleTxt, artistTxt, startDurationTxt, endDurationTxt;

    private Boolean haveFavAdded = false;
    private Boolean loopMusic = false;
    private Boolean shuffleMusic = false;
    private Boolean favSongAdd = false;

    private List<Object> playlistSongdata;
    ArrayList<SongData> musicList = new ArrayList<SongData>();
    SeekBar seekbar, volumebar;
    Handler handler = new Handler();

    SongPlaylistData songPlaylistData;
    String currentIndexes;
    private String userId;
    int progress;
    pbarThread pBarthread = new pbarThread();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        //Auth stuff
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        databaseReference.child("Users").child(userId);


        //Get Data from activity
        Bundle songData = this.getIntent().getExtras();
        currentIndexes = songData.getString("songData");

        if (currentIndexes != null){
            //Inputs all song data into musicList
            musicList.addAll(HomePage.songCollection);
            currentIndex = musicCollection.searchSongById(currentIndexes, musicList);
        }else{
            playlistID = songData.getString("playlistID");
            getPlaylistData();
        }

        // Init all id frontend stuff
        startDurationTxt = findViewById(R.id.txtStartDuration);
        endDurationTxt = findViewById(R.id.txtEndDuration);

        btnfavSong = findViewById(R.id.btnfavSong);
        btnShuffle = findViewById(R.id.btnShuffle);
        btnLoop = findViewById(R.id.btnLoop);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        seekbar = findViewById(R.id.seekBar);
        volumebar = findViewById(R.id.volumeBar);


        // Get song and play it
        displaySongBasedOnIndex();
        loadSong(fileLink);
        //Seekbar
        seekbar.setMax(player.getDuration());

        //Volumebar
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumebar.setMax(maxVolume);
        volumebar.setProgress(currentVolume);


        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(player != null && !player.isPlaying()){
                    player.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                if(player != null && player.isPlaying()){
                    player.seekTo(seekBar.getProgress());
                }
            }
        });
        volumebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


        });

        // Runs Pbar in thread
        pBarthread.isRunning = true;
        pBarthread.start();
    }

    // Login Authentication Check
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(PlaySongActivity.this, Login.class));
        }
    }

    // Threads
    private class pbarThread extends Thread{
        volatile boolean isRunning = true;
        @Override
        public void run() {
            if(!isRunning) { return; }
            try {
                if (player != null && player.isPlaying()) {
                    //Log.d("seekBar", "seekbar running");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!isRunning) { return; }
                            String startTime = createTimeStartEnd(player.getCurrentPosition(), player.getDuration())[0];
                            String endTime = createTimeStartEnd(player.getCurrentPosition(), player.getDuration())[1];
                            seekbar.setProgress(player.getCurrentPosition());
                            handler.postDelayed(this, 1000);
                            startDurationTxt.setText(startTime);
                            endDurationTxt.setText(endTime);

                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private String[] createTimeStartEnd(int startduration, int duration) {
            String time, maxtime;
            int min_duration = (duration / 1000) / 60;
            int sec_duration = (duration / 1000) % 60;
            int min_startduration = (startduration / 1000) / 60;
            int sec_startduration = (startduration / 1000) % 60;

            if (sec_duration < 10) maxtime = Integer.toString(min_duration) + ":0" + Integer.toString(sec_duration);
            else maxtime = Integer.toString(min_duration) + ":"+ Integer.toString(sec_duration);
            if (sec_startduration < 10) time = Integer.toString(min_startduration) + ":0"+ Integer.toString(sec_startduration);
            else time = Integer.toString(min_startduration) + ":"+ Integer.toString(sec_startduration);

            return new String[]{time, maxtime};
        }
    }

    private void getPlaylistData(){
        SongPlaylistData songPlaylistData = musicCollection.getPlaylistDetails(playlistID);

        //For Song Playlist Data
        playlistSongdata = (List<Object>) songPlaylistData.getSongsID();

        // Convert String SongID to Song for musicList
        for (int i = 0; i < playlistSongdata.size(); i++) {
            int tempIndex = musicCollection.searchSongById(playlistSongdata.get(i).toString(), HomePage.songCollection);
            SongData songdata = musicCollection.getCurrentSong(tempIndex, HomePage.songCollection);
            musicList.add(songdata);
        }
    }
    private void displaySongBasedOnIndex() {
        SongData song = musicCollection.getCurrentSong(currentIndex, musicList);

        // For image left and right
        int prevSongIndex = musicCollection.getNextPreviousSong(currentIndex, "btnskipPrevious", musicList);
        int nextSongIndex = musicCollection.getNextPreviousSong(currentIndex, "btnskipNext", musicList);
        SongData songPrev = musicCollection.getCurrentSong(prevSongIndex, musicList);
        SongData songNext = musicCollection.getCurrentSong(nextSongIndex, musicList);


        //Getting stuff
        title = song.getSongTitle();
        artiste = song.getSongArtist();
        fileLink = song.getSongFileLink();
        drawableLink = song.getSongImageLink();
        songid = song.getId();

        //Init components
        titleTxt = findViewById(R.id.songText);
        artistTxt = findViewById(R.id.artistText);
        songImg = findViewById(R.id.playlistImg);
        songPrevImg = findViewById(R.id.img_LeftSong);
        songNextImg = findViewById(R.id.img_RightSong);

        //Setting default current song stuff
        titleTxt.setText(title);
        artistTxt.setText(artiste);
        songImg.setContentDescription(songid);
        Picasso.get().load(drawableLink).into(songImg);


        //Prev & Next Song Image
        Picasso.get().load(songPrev.getSongImageLink()).into(songPrevImg);
        Picasso.get().load(songNext.getSongImageLink()).into(songNextImg);

        //Check for same data in favList
        String currentSongID = song.getId();
        for (int i = 0; i < LibraryPage.favList.size(); i++) {
            SongData tempsong = LibraryPage.favList.get(i);
            String favSongID = tempsong.getId();
            if (currentSongID.equals(favSongID)){
                btnfavSong.setImageResource(R.drawable.addfavtick);
                haveFavAdded = true;
                return;
            }else{
                btnfavSong.setImageResource(R.drawable.addfav);
                haveFavAdded = false;
            }
        }
    }
    public void loadSong(String url){
        try{
                player.reset();
                player.setDataSource(this, Uri.parse(url));
                player.prepare();
                player.start();
                btnPlayPause.setImageResource(R.drawable.pauseicon);
                seekbar.setMax(player.getDuration());
                gracefullyStopMusic();
        }catch (Exception e) {
            Log.d("loadSong", "Exception : " + e);
        }
    }


    // Play and Pause music button input
    public void playerPauseMusic(View view){
        try {
            if (player != null) {
                if (player.isPlaying()) {
                    player.pause();
                    btnPlayPause.setImageResource(R.drawable.playicon);

                } else {
                    player.start();
                    btnPlayPause.setImageResource(R.drawable.pauseicon);

                    seekbar.setMax(player.getDuration());

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void eqHandler(View view){

    }
    public void backBtn(View view){ onBackPressed();}
    public void addFav(View view) {
        SongData song = musicCollection.getCurrentSong(currentIndex, musicList);

        //Check for same data in favList
        checkforFavData(song);

        if (haveFavAdded == false) { //havent press
            haveFavAdded = true;
            LibraryPage.favList.add(song);

            // Pumping into json to save later
//            String json = gson.toJson(LibraryPage.favList);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("myPlaylistList", json);
//            editor.apply();

            Log.d("btnwhy", song.getSongTitle() + " Apply");
            // When task completes
            btnfavSong.setImageResource(R.drawable.addfavtick);
            Toast.makeText(this, "Song saved from playlist!", Toast.LENGTH_SHORT).show();
        } else { //pressed
            haveFavAdded = false;
            //Get index of favList and check with current
            getIndexofFavData(song);

            LibraryPage.favList.remove(index);

            // Pumping into json to save later
//            String json = gson.toJson(LibraryPage.favList);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("myPlaylistList", json);
//            editor.apply();

            btnfavSong.setImageResource(R.drawable.addfav);
            Toast.makeText(this, "Song removed from playlist!", Toast.LENGTH_SHORT).show();

        }
    }
    //Repeats songs or loop current song for button input
    public void playerRepeatMusic(View view){
        try{
            if (loopMusic == false){
                loopMusic = true;
                btnLoop.setImageResource(R.drawable.loopiconpinkred);
                //repeatSongs = false;
                //btnRepeatSongs.setText("REPEAT");
            }else{
                loopMusic = false;
                btnLoop.setImageResource(R.drawable.loopiconwhite);

            }
        }catch(Exception e){
            Log.d("PlayerRepeatBtn", "Exception: " + e);
        }
    }
    public void playerShuffleMusic(View view){
        try{
            if (shuffleMusic == false){
                shuffleMusic = true;
                btnShuffle.setImageResource(R.drawable.shuffleiconpinkred);
                musicList = musicCollection.getShuffleList(HomePage.songCollection);

            }else{
                shuffleMusic = false;
                btnShuffle.setImageResource(R.drawable.shuffleiconwhite);
                // Set back to original
                HomePage.songCollection.addAll(musicList);
            }
        }catch(Exception e){
            Log.d("PlayerRepeatBtn", "Exception: " + e);
        }
    }

    public void playNextPreviousMusic(View view){
        String btnID = getResources().getResourceEntryName(view.getId());
        currentIndex = musicCollection.getNextPreviousSong(currentIndex, btnID, musicList);
        displaySongBasedOnIndex();
        loadSong(fileLink);
    }


    //Check for favData
    private void checkforFavData(SongData song){
        String currentSongID = song.getId();
        for (int i = 0; i < LibraryPage.favList.size(); i++) {
            SongData tempsong = LibraryPage.favList.get(i);
            String favSongID = tempsong.getId();
            if (currentSongID.equals(favSongID)){
                Log.d("checkData", "currentSongID :" + currentSongID);
                Log.d("checkData", "favSongID :" + favSongID);
                haveFavAdded = true;
                return;
            }else{
                haveFavAdded = false;
            }
        }
    }
    //Remove favData by songID | To get index of it to delete from fav data
    private void getIndexofFavData(SongData song){
        String currentSongID = song.getId();
        // Get index of same song from favList
        for (int i = 0; i < LibraryPage.favList.size(); i++) {
            SongData tempsong = LibraryPage.favList.get(i);
            String favSongID = tempsong.getId();
            if (currentSongID.equals(favSongID)){
                index = i;
                return;
            }else{
                ;
            }
        }
    }


    // Listen for music ends and run this
    private void gracefullyStopMusic(){
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer){
                Log.d("name", "Music completed!");
                if (loopMusic == true){
                    player.start();
                    seekbar.setMax(player.getDuration());

                }else if(shuffleMusic == true){
                    currentIndex = musicCollection.getNextPreviousSong(currentIndex, "btnskipNext", musicList);
                    displaySongBasedOnIndex();
                    loadSong(fileLink);
                }else{
                    currentIndex = musicCollection.getNextPreviousSong(currentIndex, "btnskipNext", musicList);
                    displaySongBasedOnIndex();
                    loadSong(fileLink);
                }
            }
        });
    }

    //Android phone listeners
    //Listens when android back btn is pressed
    @Override
    public void onBackPressed() {
        if (player != null) {
            pBarthread.isRunning = false;
            pBarthread.interrupt();
            player.stop();
            player.release();
            player = null;
        }
        super.onBackPressed();
    }

    //Volume bar listener
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
//            progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        }
//        return true;
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            volumebar = (SeekBar) findViewById(R.id.volumeBar);
            int index = volumebar.getProgress();
            volumebar.setProgress(index + 1);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            int index = volumebar.getProgress();
            volumebar.setProgress(index - 1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public void onPause() {
//        super.onPause();  // Always call the superclass method first
//        player.pause();
//        pBarthread.isRunning = false;
//        pBarthread.interrupt();
//        // stop the clock
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();  // Always call the superclass method first
//        player.start();
//        pBarthread.isRunning = true;
//        pBarthread.start();
//        // re-sync the clock with player...
//    }
}
