package com.example.musicapp.Model;

public class SliderItem {
    private int imageResId;
    private String title;

    public SliderItem(int imageResId, String title) {
        this.imageResId = imageResId;
        this.title = title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }
}
