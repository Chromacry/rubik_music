package com.tp.musicapp_project.GenerateData;

import com.tp.musicapp_project.HomePage;
import com.tp.musicapp_project.LibraryPage;
import com.tp.musicapp_project.MainActivity;

import java.util.ArrayList;
import java.util.Collections;

public class MusicCollection {
    public MusicCollection() {
    }

    //Return index of data arraylist
    public int searchSongById(String id, ArrayList<SongData> song) {
        for (int i = 0; i < song.size(); i++) {
            SongData tempsong = song.get(i);
            String tempID = tempsong.getId();
            if (tempID.equals(id)) {
                return i;
            }
        }
        return 0;
    }

    // get index of next/previous song
    public int getNextPreviousSong(int index, String choice, ArrayList<SongData> song) {
        if (choice.equals("btnskipPrevious") && !(index <= 0)) {
            index--;
            return index;
        } else if (choice.equals("btnskipPrevious") && index == 0) {
            index = song.size()-1;
            return index;
        }else if(choice.equals("btnskipNext") && !(index > song.size()-2)){
            index++;
            return index;
        }else if(choice.equals("btnskipNext") && index > song.size()-2){
            return 0;
        }else {
            return index;
        }
    }

    //get current index of song in arraylist
    public SongData getCurrentSong(int index, ArrayList<SongData> song) {
        return song.get(index);
    }

    public ArrayList getShuffleList(ArrayList<SongData> song){
        ArrayList<SongData> shuffleList = new ArrayList<SongData>();
        shuffleList.addAll(song);
            Collections.shuffle(shuffleList);
        return shuffleList;
    }

    //Playlist Stuff
    public SongPlaylistData getPlaylistDetails(String songID){
        for (int i = 0; i < MainActivity.playlistCollection.size(); i++) {
            String dataSongID = MainActivity.playlistCollection.get(i).id;
            if (songID.equals(dataSongID)){
                SongPlaylistData songPlaylistData = MainActivity.playlistCollection.get(i);
                return songPlaylistData;
            }
        }
        return null;
    }
}
