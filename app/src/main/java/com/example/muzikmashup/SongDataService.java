package com.example.muzikmashup;

import java.util.Map;

/***
 * Song data service. This service will be used to actively save song data, like total times
 * a song is played or the total time a song is played, and then saved to internal storage. Users
 * can play their playlists with this recorded data
 */
public class SongDataService implements ISongDataService
{
    public SongDataService(ShuffleType shuffleType)
    {
        // Open the file corresponding to how the user wants their music to be played, i.e., regular shuffle, times played, etc
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
