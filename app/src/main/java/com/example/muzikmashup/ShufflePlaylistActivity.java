package com.example.muzikmashup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

public class ShufflePlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuffle_playlist);
        Bundle playlistBundle = this.getIntent().getExtras();
        if (playlistBundle != null) {
            // Shuffle music
            final Playlist playlistInfo = (Playlist) playlistBundle.getSerializable("playlistInfo");
            playMusic(playlistInfo.songs.get(0).getId());
        }
    }

    public void playMusic(long songId){
        MediaPlayer mpObject = new MediaPlayer();
        try {
            if (songId > 0) {
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
}
