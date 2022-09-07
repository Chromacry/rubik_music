package com.tp.musicapp_project.GenerateData;

import java.util.ArrayList;

public class GeneratePlaylistData {
    private String id, playlistTitle, playlistArtist, songGenre, songCategory, songImageLink;
    private ArrayList songsID;

    public GeneratePlaylistData(String id, String playlistTitle, String playlistArtist, String songGenre, String songCategory, String songImageLink, ArrayList songsID) {
        this.id = id;
        this.playlistTitle = playlistTitle;
        this.playlistArtist = playlistArtist;
        this.songGenre = songGenre;
        this.songCategory = songCategory;
        this.songImageLink = songImageLink;
        this.songsID = songsID;
    }

    public String getId() {
        return id;
    }


    public String getPlaylistTitle() {
        return playlistTitle;
    }


    public String getPlaylistArtist() {
        return playlistArtist;
    }


    public String getSongGenre() {
        return songGenre;
    }


    public String getSongCategory() {
        return songCategory;
    }


    public String getSongImageLink() {
        return songImageLink;
    }


    public ArrayList getSongsID() {
        return songsID;
    }
}
