package com.example.muzikmashup;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
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
        List<String> playlistNames = new ArrayList<>();
        getPlaylistNames(playlists, playlistNames);

        if (playlistNames.size() == 0){
            // Display no playlists!
            TextView tv1 = (TextView)findViewById(R.id.textIntroInner);
            tv1.setText("No playlist found. Create some playlists in Google Play Music");
        }
        else{
            createPlaylistButtons(playlistNames);
        }
        // TODO: make button event handlers. When selecting a playlist, send those songs over to new activity to pick which way to play
        //setContentView(R.layout.activity_main);
    }

    // Get playlist names to display
    public void getPlaylistNames(Cursor playlists, List<String> playlistNames){
        for(int i = 0; i < playlists.getCount(); i++)
        {
            playlists.moveToPosition(i);
            playlistNames.add(playlists.getString(playlists.getColumnIndex("playlist_name")));
        }
    }

    // Create buttons for each playlist
    public void createPlaylistButtons(List<String> playlistNames){
        LinearLayout layout = (LinearLayout)findViewById(R.id.activity_main);

        for (int b = 0; b < playlistNames.size(); b++){
            String playlistName = playlistNames.get(b);

            Button btn = new Button(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(60, 0, 60, 0);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            btn.setText(playlistName);
            btn.setId(0);  // not sure this matters. just setting to 0 for all for now
            //btn.setMinimumHeight(150 + h);
            layout.addView(btn, layoutParams);
        }
    }
}
