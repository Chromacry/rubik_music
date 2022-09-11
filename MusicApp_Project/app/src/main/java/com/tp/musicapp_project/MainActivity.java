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
    runPbarThread runPbarThread = new runPbarThread();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setScaleY(2f);

        //progressbar to change activity
        runPbarThread.isRunning = true;
        runPbarThread.start();

    }
    //Thread
    private class runPbarThread extends Thread {
        volatile boolean isRunning = true;

        @Override
        public void run() {
            if (!isRunning) {
                return;
            }
            try {
                progressAnimation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void progressAnimation() {
            ProgressAnimation animation = new ProgressAnimation(MainActivity.this, progressBar, 0f, 100f);
            animation.setDuration(4000);
            progressBar.setAnimation(animation);
        }
    }


}
//    Query querySearch = dbSongData.orderByChild("songTitle"); //Search specifically
//        querySearch.addListenerForSingleValueEvent(valueEventListener_song);