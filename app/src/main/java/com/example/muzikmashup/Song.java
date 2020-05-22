package com.example.muzikmashup;

import java.io.Serializable;

// Class for a song. Song consists of the title and its Id to play it
public class Song implements Serializable {
    public String title;
    public long id;

    public Song(String title, Long id){
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
