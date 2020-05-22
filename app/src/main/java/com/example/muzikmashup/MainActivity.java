package com.example.muzikmashup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return;
        }
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode)
        {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // Permission granted. No op
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    // Get playlist's info. This includes the name, id, and all of its songs
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
        String[] queryParams = { "SourceId", MediaStore.Audio.Playlists.Members.TITLE, MediaStore.Audio.Playlists.Members._ID };
        String playListRef = "content://com.google.android.music.MusicContent/playlists/" + playlistId + "/members";
        Uri songUri = Uri.parse(playListRef);
        Cursor songCursor = getContentResolver().query(songUri, queryParams, null, null, null);
        for (int x = 0; x < songCursor.getCount(); x++){
            songCursor.moveToPosition(x);
            String songName = songCursor.getString(songCursor.getColumnIndex("title"));
            Long songId = songCursor.getLong(songCursor.getColumnIndex("SourceId"));
            songList.add(new Song(songName, songId));
        }
        songCursor.close();
    }

    // Create buttons for each playlist
    public void createPlaylistButtons(List<Playlist> playlistInfo){
        LinearLayout layout = (LinearLayout)findViewById(R.id.activity_main);

        for (int b = 0; b < playlistInfo.size(); b++){
            Playlist playlist = playlistInfo.get(b);

            Button btn = new Button(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(60, 0, 60, 0);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            btn.setText(playlist.playlistName);
            btn.setId(0);  // not sure this matters. just setting to 0 for all for now
            createButtonEvent(btn, playlist);
            layout.addView(btn, layoutParams);
        }
    }

    // Create events for each selectable playlist
    public void createButtonEvent(final Button btn, final Playlist playlist){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlaylistActivity.class);
                Bundle playlistBundle = new Bundle();
                playlistBundle.putSerializable("playlistInfo", playlist);
                intent.putExtras(playlistBundle);
                startActivity(intent);
            }
        });
    }
}
