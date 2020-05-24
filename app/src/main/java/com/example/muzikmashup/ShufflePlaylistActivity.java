package com.example.muzikmashup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ShufflePlaylistActivity extends AppCompatActivity {

    // Number of songs that haven't been played yet
    int songsLeftSize;
    // To randomly shuffle songs
    private static Random songPicker = new Random();
    // Songs that have not been played yet
    private static List<Song> songsLeftToPlay = new ArrayList<>();
    // The order of which songs are played, in case the user wants to play a previous song
    private static List<Song> songPlayOrder = new LinkedList<>();
    // Media player to play the song
    private static MediaPlayer mpObject = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuffle_playlist);
        Bundle playlistBundle = this.getIntent().getExtras();
        if (playlistBundle != null) {
            createNextButtonEvent((ImageButton) findViewById(R.id.nextSongButton));
            // Shuffle music
            final Playlist playlistInfo = (Playlist) playlistBundle.getSerializable("playlistInfo");
            cloneList(playlistInfo.songs);
            songsLeftSize = songsLeftToPlay.size();
            // On song completion, play the next song
            mpObject.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mpObject.reset();
                    playMusic(getNextSong());
                }
            });
            shuffleMusic();
        }
    }

    public void shuffleMusic() {
        // Play the music in the playlist!
        if (songsLeftSize >= 1){
            playMusic(getNextSong());
        }
    }

    // Get the next song to play
    private Song getNextSong() {
        // Generate a random number. This will be used to pick an index of songs left to play
        int nextSong = songPicker.nextInt(songsLeftSize);
        Song songToPlay = songsLeftToPlay.get(nextSong);
        // Subtract the number of songs left by 1
        songsLeftSize--;
        // Add this song to the linkedlist so we have the order the songs were played
        songPlayOrder.add(songToPlay);
        return songToPlay;
    }

    // Play the next song
    public void playMusic(Song songToPlay){
        long songId = songToPlay.getId();
        try {
            if (songId > 0) {
                switchSongTitle(songToPlay.getTitle());
                Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.valueOf(songId));
                mpObject.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mpObject.setDataSource(this, contentUri);
                mpObject.prepare();
                mpObject.start();
                songsLeftToPlay.remove(songToPlay);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Switching the actively playing song title
    public void switchSongTitle(String title){
        TextView titleText = (TextView)findViewById(R.id.titleSong);
        titleText.setText(title);
    }

    // Button for playing the next song
    public void createNextButtonEvent(ImageButton nextSongButton){
        nextSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpObject.reset();
                if (songsLeftSize >= 1){
                    playMusic(getNextSong());
                }
            }
        });
    }

    // Need a deep copy of the songsLeftToPlay
    public static void cloneList(List<Song> songs) {
        for (Song songToAdd : songs) {
            songsLeftToPlay.add(new Song(songToAdd.title, songToAdd.id));
        }
    }
}
