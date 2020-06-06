package com.example.muzikmashup;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/***
 * Song data service. This service will be used to actively save song data, like total times
 * a song is played or the total time a song is played, and then saved to internal storage. Users
 * can play their playlists with this recorded data
 */
public class SongDataService implements ISongDataService
{
    private FileOutputStream fileOutputStream;
    private FileInputStream fileInputStream;

    public SongDataService(ShuffleType shuffleType, Context context) throws IOException {

        // Open the file corresponding to how the user wants their music to be played, i.e., regular shuffle, times played, etc
        File playFile = new File(shuffleType.toString());
        playFile.createNewFile(); // if file already exists will do nothing

        // Create the output stream to write to the file
        fileOutputStream = context.openFileOutput(playFile.toString(), Context.MODE_PRIVATE);

        // Create the input stream to read from the file
        fileInputStream = context.openFileInput(playFile.toString());
    }

    public void updateSongTimePlayed(Song song)
    {

    }

    public void updateTimesSongHasBeenPlayed(Song song)
    {

    }

    public Map<String, Long> getSongTimePlayedValues()
    {
        return null;
    }

    public Map<String, Integer> getTimesSongHasBeenPlayedValues()
    {
        return null;
    }

    public void calculatePercentOfSongPlayed(Song song)
    {

    }
}
