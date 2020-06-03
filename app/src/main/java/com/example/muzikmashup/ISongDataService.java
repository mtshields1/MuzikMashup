package com.example.muzikmashup;

import java.util.Map;

/***
 * Interface for the song data service
 */
public interface ISongDataService
{
    void updateSongTimePlayed(Song song);

    void updateTimesSongHasBeenPlayed(Song song);

    Map<String, Long> getSongTimePlayedValues();

    Map<String, Integer> getTimesSongHasBeenPlayedValues();

    void calculatePercentOfSongPlayed(Song song);
}
