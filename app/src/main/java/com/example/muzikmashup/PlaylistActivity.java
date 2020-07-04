package com.example.muzikmashup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/***
 * Activity that displays possible ways to play a playlist
 */
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
            createSongButtonEvent((ImageButton) findViewById(R.id.shuffleButton), playlistInfo);
            createSongsPlayedButtonEvent((Button) findViewById(R.id.songsPlayedButton));
        }
        else {
            // no songs found?
        }
    }

    public void createSongButtonEvent(ImageButton button, final Playlist playlist){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShufflePlaylistActivity.class);
                Bundle playlistBundle = new Bundle();
                playlistBundle.putSerializable("playlistInfo", playlist);
                intent.putExtras(playlistBundle);
                startActivity(intent);
            }
        });
    }

    public void createSongsPlayedButtonEvent(Button songsPlayedButton){
        songsPlayedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PlaylistActivity.this);
                builder.setTitle("Times Songs Have Been Played");
            }
        });
    }
}
