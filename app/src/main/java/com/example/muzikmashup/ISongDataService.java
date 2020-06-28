package com.example.muzikmashup;

import java.util.Map;

/***
 * Interface for the song data service
 */
public interface ISongDataService
{
    void updateSongTimePlayed(Song song, int songTimePlayed);

    void updateTimesSongHasBeenPlayed(Song song);

    Map<String, Integer> getSongTimePlayedValues();

    Map<String, Integer> getTimesSongHasBeenPlayedValues();

    long calculatePercentOfSongPlayed(long songTimePlayed, long totalSongTime);
}
