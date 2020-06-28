package com.example.muzikmashup;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
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
    private ObjectOutputStream outputStream;
    private ObjectInputStream objectInputStream;
    // Map to hold the amount of times a song has been played
    private HashMap<String, Integer> timesSongHasBeenPlayed;
    // Map to hold the total time a song has been listened to
    private HashMap<String, Integer> totalTimeSongHasBeenListenedTo;

    public SongDataService(ShuffleType shuffleType, Context context) throws IOException, ClassNotFoundException {

        // Open the file corresponding to how the user wants their music to be played, i.e., regular shuffle, times played, etc
        File playFile = new File(shuffleType.toString());
        boolean fileDoesNotExist = playFile.createNewFile(); // if file already exists will do nothing

        // Create the output stream to write to the file
        fileOutputStream = context.openFileOutput(playFile.toString(), Context.MODE_PRIVATE);
        outputStream = new ObjectOutputStream(fileOutputStream);

        // Create the input stream to read from the file
        fileInputStream = context.openFileInput(playFile.toString());
        objectInputStream = new ObjectInputStream(fileInputStream);
        getFileMap(shuffleType, fileDoesNotExist);
    }

    @SuppressWarnings("unchecked")
    public void getFileMap(ShuffleType shuffleType, boolean fileDoesNotExist) throws IOException, ClassNotFoundException {
        if (!fileDoesNotExist && shuffleType.toString().equals(ShuffleType.SONG_TIMES_PLAYED.toString())) {
            timesSongHasBeenPlayed = (HashMap<String, Integer>)objectInputStream.readObject();
        }
        else {
            timesSongHasBeenPlayed = new HashMap<>();
        }
        if (!fileDoesNotExist && shuffleType.toString().equals(ShuffleType.SONG_TOTAL_TIME_PLAYED.toString())) {
            totalTimeSongHasBeenListenedTo = (HashMap<String, Integer>)objectInputStream.readObject();
        }
        else {
            totalTimeSongHasBeenListenedTo = new HashMap<>();
        }
    }

    public void updateSongData(Song song, int songTimePlayed, int totalSongTime) {
        // TODO: Calculate percentage of song played, then call updateSongTimePlayed and updateTimesSongHasBeenPlayed (if time was greater than 10 seconds)
        // Only update a song's total play time and total times played if 10% or more of the song was listened to. This is because while shuffling
        // through songs, the user still needs to see the song to skip it, hence they will "listen" to at least some portion of it
        if (calculatePercentOfSongPlayed(songTimePlayed, totalSongTime) >= .1){
            updateSongTimePlayed(song, songTimePlayed);
            updateTimesSongHasBeenPlayed(song);
        }
    }

    public void updateSongTimePlayed(Song song, int songTimePlayed)
    {
        if (totalTimeSongHasBeenListenedTo.containsKey(song.title)){
            totalTimeSongHasBeenListenedTo.put(song.title, totalTimeSongHasBeenListenedTo.get(song.title) + songTimePlayed);
        }
        else {
            totalTimeSongHasBeenListenedTo.put(song.title, songTimePlayed);
        }
    }

    public void updateTimesSongHasBeenPlayed(Song song)
    {
        if (timesSongHasBeenPlayed.containsKey(song.title)){
            timesSongHasBeenPlayed.put(song.title, timesSongHasBeenPlayed.get(song.title) + 1);
        }
        else {
            timesSongHasBeenPlayed.put(song.title, 1);
        }
    }

    public Map<String, Integer> getSongTimePlayedValues() {
        return totalTimeSongHasBeenListenedTo;
    }

    public Map<String, Integer> getTimesSongHasBeenPlayedValues() {
        return timesSongHasBeenPlayed;
    }

    public long calculatePercentOfSongPlayed(long songTimePlayed, long totalSongTime)
    {
        return songTimePlayed / totalSongTime;
    }
}
