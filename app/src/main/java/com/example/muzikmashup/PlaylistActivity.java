package com.example.muzikmashup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class PlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Bundle playlistBundle = this.getIntent().getExtras();
        if (playlistBundle != null) {
            // Populate the playlist screen
            final Playlist playlistInfo = (Playlist) playlistBundle.getSerializable("playlistInfo");
            TextView titleText = (TextView)findViewById(R.id.titlePlaylist);
            titleText.setText(playlistInfo.playlistName);
        }
        else {
            // no songs found?
        }
    }
}
