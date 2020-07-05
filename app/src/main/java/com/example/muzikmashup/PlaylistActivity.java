package com.example.muzikmashup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * Activity that displays possible ways to play a playlist
 */
public class PlaylistActivity extends AppCompatActivity {

    String[] names = {"India", "Brazil", "Argentina",
            "Portugal", "France", "England", "Italy"};
    ArrayAdapter<String> adapter;
    ListView listView;

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
                SongDataService songDataService = null;
                try {
                    songDataService = new SongDataService(ShuffleType.SONG_TIMES_PLAYED, getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Map<String, Integer> timesSongsHaveBeenPlayed = songDataService.getTimesSongHasBeenPlayedValues();
                List<String> list = new ArrayList<String>(timesSongsHaveBeenPlayed.keySet());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlaylistActivity.this);
                View rowList = getLayoutInflater().inflate(R.layout.music_list, null);
                listView = rowList.findViewById(R.id.listView);
                adapter = new ArrayAdapter<String>(PlaylistActivity.this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                alertDialog.setTitle("Times Songs Have Been Played");
                alertDialog.setView(rowList);
                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }
        });
    }
}
