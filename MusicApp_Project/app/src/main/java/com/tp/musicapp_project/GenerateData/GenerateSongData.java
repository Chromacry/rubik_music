package com.tp.musicapp_project.GenerateData;

public class GenerateSongData {
    private String id, songTitle, songArtist, songGenre, songCategory, songFileLink, songImageLink;

    public GenerateSongData(String id, String songTitle, String songArtist, String songGenre, String songCategory, String songFileLink, String songImageLink) {
        this.id = id;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.songGenre = songGenre;
        this.songCategory = songCategory;
        this.songFileLink = songFileLink;
        this.songImageLink = songImageLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public String getSongGenre() {
        return songGenre;
    }

    public void setSongGenre(String songGenre) {
        this.songGenre = songGenre;
    }

    public String getSongCategory() {
        return songCategory;
    }

    public void setSongCategory(String songCategory) {
        this.songCategory = songCategory;
    }

    public String getSongFileLink() {
        return songFileLink;
    }

    public void setSongFileLink(String songFileLink) {
        this.songFileLink = songFileLink;
    }

    public String getSongImageLink() {
        return songImageLink;
    }

    public void setSongImageLink(String songImageLink) {
        this.songImageLink = songImageLink;
    }
}
