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
import java.util.Stack;

public class ShufflePlaylistActivity extends AppCompatActivity {

    // Number of songs that haven't been played yet
    int songsLeftSize;
    // To randomly shuffle songs
    private static Random songPicker = new Random();
    // Songs that have not been played yet
    private static List<Song> songsLeftToPlay = new ArrayList<>();
    // The order of which songs are played, in case the user wants to play a previous song
    private static LinkedList<Song> songPlayOrder = new LinkedList<>();
    // Add a song to this stack each time the user wants to play a previously played song
    private static Stack<Song> previousSongPlayOrder = new Stack<>();
    // Media player to play the song
    private static MediaPlayer mpObject = new MediaPlayer();
    // If the user plays previous songs
    private boolean playingPreviousSongs = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuffle_playlist);
        Bundle playlistBundle = this.getIntent().getExtras();
        if (playlistBundle != null) {
            createNextButtonEvent((ImageButton) findViewById(R.id.nextSongButton));
            createPreviousButtonEvent((ImageButton) findViewById(R.id.previousSongButton));
            // Shuffle music
            final Playlist playlistInfo = (Playlist) playlistBundle.getSerializable("playlistInfo");
            cloneList(playlistInfo.songs);
            songsLeftSize = songsLeftToPlay.size();
            // On song completion, assess the state of the music being played
            mpObject.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    assessMusicState();
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
        int nextSongId = songPicker.nextInt(songsLeftSize);
        Song songToPlay = songsLeftToPlay.get(nextSongId);
        // Subtract the number of songs left by 1
        songsLeftSize--;
        // Add this song to the linkedlist so we have the order the songs were played
        songPlayOrder.add(songToPlay);
        songsLeftToPlay.remove(songToPlay);
        return songToPlay;
    }

    // Play the next song
    public void playMusic(Song songToPlay){
        // The user has finished playing previously played music (stack is empty)
        if (playingPreviousSongs && previousSongPlayOrder.size() == 0){
            playingPreviousSongs = false;
        }
        long songId = songToPlay.getId();
        try {
            if (songId > 0) {
                switchSongTitle(songToPlay.getTitle());
                Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.valueOf(songId));
                mpObject.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mpObject.setDataSource(this, contentUri);
                mpObject.prepare();
                mpObject.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Button for playing the next song
    public void createNextButtonEvent(ImageButton nextSongButton){
        nextSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assessMusicState();
            }
        });
    }

    public void createPreviousButtonEvent(ImageButton previousSongButton){
        previousSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpObject.reset();
                // First, ensure a song has been played previously. If not, this is the first song in the list. Play it from the start
                if (songPlayOrder.size() == 1){
                    // Play song from beginning
                    playMusic(songPlayOrder.peekFirst());
                }
                else{
                    // Add the currently played song to the stack. Any additional songs to be played again will be added
                    previousSongPlayOrder.push(songPlayOrder.removeLast());
                    playingPreviousSongs = true;
                    // Play the last song that was played (one before the one currently playing)
                    playMusic(songPlayOrder.peekLast());
                }
            }
        });
    }

    /*
     Method used to assess the state of music being played. There are 2 potential states as follows
     1. The user pressed the previous button several times. Need to go through song play order stack and pop the songs and play them
     2. Songs are being played as usual. Get, and play, a new song
     */
    public void assessMusicState(){
        // The user was playing a previously played song. Advance forward in the song play order
        if (playingPreviousSongs){
            mpObject.reset();
            Song song = previousSongPlayOrder.pop();
            songPlayOrder.add(song);
            playMusic(song);
        }
        // Play a new, unplayed song
        else{
            mpObject.reset();
            if (songsLeftSize >= 1){
                playMusic(getNextSong());
            }
        }
    }

    // Switching the actively playing song title
    public void switchSongTitle(String title){
        TextView titleText = (TextView)findViewById(R.id.titleSong);
        titleText.setText(title);
    }

    // Need a deep copy of the songsLeftToPlay
    public static void cloneList(List<Song> songs) {
        for (Song songToAdd : songs) {
            songsLeftToPlay.add(new Song(songToAdd.title, songToAdd.id));
        }
    }
}
