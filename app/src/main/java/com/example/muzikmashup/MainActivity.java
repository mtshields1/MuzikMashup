package com.example.muzikmashup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Uri playlistsUri = Uri.parse("content://com.google.android.music.MusicContent/playlists");
        Cursor playlists = getApplicationContext().getContentResolver().query(playlistsUri, new String[]{"_id", "playlist_name"}, null, null, null);
        List<Playlist> playlistInfo = new ArrayList<>();
        getPlaylistDetails(playlists, playlistInfo);

        if (playlistInfo.size() == 0){
            // Display no playlists!
            TextView titleText = (TextView)findViewById(R.id.textIntroInner);
            titleText.setText("No playlist found. Create some playlists in Google Play Music");
        }
        else{
            createPlaylistButtons(playlistInfo);
        }
        System.out.println(playlistInfo);
        // TODO: make button event handlers. When selecting a playlist, send those songs over to new activity to pick which way to play
    }

    // Get playlist names to display
    public void getPlaylistDetails(Cursor playlists, List<Playlist> playlistInfo){
        List<String> songNames = new ArrayList<>();
        for(int i = 0; i < playlists.getCount(); i++)
        {
            playlists.moveToPosition(i);
            String playlistName = playlists.getString(playlists.getColumnIndex("playlist_name"));
            Long playlistId = playlists.getLong(playlists.getColumnIndex("_id"));
            List<Song> songList = new ArrayList<>();
            getPlaylistSongs(playlistId, songList);
            playlistInfo.add(new Playlist(playlistName, playlistId, songList));
        }
    }

    // Populate the playlist's list of songs
    public void getPlaylistSongs(Long playlistId, List<Song> songList){
        String[] queryParams = { MediaStore.Audio.Playlists.Members.TITLE, MediaStore.Audio.Playlists.Members._ID };
        String playListRef = "content://com.google.android.music.MusicContent/playlists/" + playlistId + "/members";
        Uri songUri = Uri.parse(playListRef);
        Cursor songCursor = getContentResolver().query(songUri, queryParams, null, null, null);
        for (int x = 0; x < songCursor.getCount(); x++){
            songCursor.moveToPosition(x);
            String songName = songCursor.getString(songCursor.getColumnIndex("title"));
            Long songId = songCursor.getLong(songCursor.getColumnIndex("_id"));
            songList.add(new Song(songName, songId));
        }
    }

    // Create buttons for each playlist
    public void createPlaylistButtons(List<Playlist> playlistInfo){
        LinearLayout layout = (LinearLayout)findViewById(R.id.activity_main);

        for (int b = 0; b < playlistInfo.size(); b++){
            String playlistName = playlistInfo.get(b).playlistName;

            Button btn = new Button(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(60, 0, 60, 0);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            btn.setText(playlistName);
            btn.setId(0);  // not sure this matters. just setting to 0 for all for now
            //createButtonEvent(btn);
            layout.addView(btn, layoutParams);
        }
    }

    // Create events for each selectable playlist
    public void createButtonEvent(final Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlaylistActivity.class);
                intent.putExtra("playlistName", btn.getText());
                startActivity(intent);
            }
        });
    }
}
