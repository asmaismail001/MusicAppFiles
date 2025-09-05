package com.example.musicapp;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String artist;
    private String image; // URL
    private String url;   // URL to MP3

    public Song() {
    } // Firebase

    public Song(String title, String artist, String image, String url) {
        this.title = title;
        this.artist = artist;
        this.image = image;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}