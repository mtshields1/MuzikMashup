package com.example.muzikmashup;

import java.util.List;

public class Playlist {
    public String playlistName;
    public long playlistId;
    public List<Song> songs;

    public Playlist(String playlistName, long playlistId, List<Song> songs){
        this.playlistName = playlistName;
        this.playlistId = playlistId;
        this.songs = songs;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(long playlistId) {
        this.playlistId = playlistId;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
