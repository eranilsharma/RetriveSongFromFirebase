package com.android.sharmaji.onlinesong.model;

public class Song {
    private String SongCategory;
    private String MusicName;
    private String Songurl;
    private String songUID;
    private int Likes;
    private String artistName;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }


    public String getSongUID() {
        return songUID;
    }

    public void setSongUID(String songUID) {
        this.songUID = songUID;
    }



    public Song() {
    }

    public Song(String songCategory, String musicName, String songurl,String songUid,int like,String artistName) {
        this.SongCategory = songCategory;
        this.MusicName = musicName;
        this.Songurl = songurl;
        this.songUID=songUid;
        this.Likes=like;
        this.artistName=artistName;
    }

    public String getSongCategory() {
        return SongCategory;
    }

    public void setSongCategory(String songCategory) {
        this.SongCategory = songCategory;
    }

    public String getMusicName() {
        return MusicName;
    }

    public void setMusicName(String musicName) {
        this.MusicName = musicName;
    }

    public String getSongurl() {
        return Songurl;
    }

    public void setSongurl(String songurl) {
        this.Songurl = songurl;
    }

}
