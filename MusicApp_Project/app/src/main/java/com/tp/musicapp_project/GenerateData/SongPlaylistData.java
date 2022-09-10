package com.tp.musicapp_project.GenerateData;

import java.util.ArrayList;
import java.util.List;

public class SongPlaylistData {
    public String id, playlistTitle, playlistArtist, songGenre, songCategory, songImageLink;
    public Object songsID;

    public SongPlaylistData(){

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

    public Object getSongsID() {
        return songsID;
    }
}
