package com.example.musicapp;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

public class SliderTimer {

    public ArrayList<Song> songList;
    public int currentIndex = 0;
    public Handler handler;
    public Runnable runnable;

    public SliderTimer(ArrayList<Song> songList) {
        this.songList = songList;
        handler = new Handler(Looper.getMainLooper());
    }

    public void startSlider(Runnable updateUIRunnable, long delayMillis) {
        runnable = new Runnable() {
            @Override
            public void run() {
                currentIndex++;
                if (currentIndex >= songList.size()) {
                    currentIndex = 0;
                }
                updateUIRunnable.run(); // call UI update in activity/fragment
                handler.postDelayed(this, delayMillis);
            }
        };
        handler.postDelayed(runnable, delayMillis);
    }

    public void stopSlider() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public Song getCurrentSong() {
        if (songList != null && !songList.isEmpty()) {
            return songList.get(currentIndex);
        }
        return null;
    }
}
