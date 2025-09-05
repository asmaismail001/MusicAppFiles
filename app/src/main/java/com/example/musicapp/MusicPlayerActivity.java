package com.example.musicapp;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerActivity extends AppCompatActivity {
    private ArrayList<Song> songList; // <-- declare this at class level
    private int songIndex;            // <-- declare this at class level
    private ImageView prevBtn, nextBtn;


    private TextView songTitle;
    private ImageView coverImagePlayer;
    private SeekBar seekBar;
    private ImageView playPauseBtn;

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private Handler handler = new Handler();

    private ObjectAnimator rotateAnimator;

    private final Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && isPlaying) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        // Views
        songTitle = findViewById(R.id.songTitle);
        coverImagePlayer = findViewById(R.id.coverImagePlayer);
        seekBar = findViewById(R.id.seekBar);
        playPauseBtn = findViewById(R.id.playBtn);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);

        // Receive Data from Intent safely
        Intent intent = getIntent();
        if (intent != null) {
            // Cast Serializable to ArrayList<Song>
            songList = (ArrayList<Song>) intent.getSerializableExtra("songList");
            songIndex = intent.getIntExtra("songIndex", 0);

            if (songList != null && songIndex >= 0 && songIndex < songList.size()) {
                // Current Song
                Song currentSong = songList.get(songIndex);

                songTitle.setText(currentSong.getTitle());

                Glide.with(this)
                        .load(currentSong.getImage())
                        .placeholder(R.drawable.img_27)
                        .into(coverImagePlayer);

                // Setup MediaPlayer
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                try {
                    mediaPlayer.setDataSource(currentSong.getUrl());
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(mp -> {
                        mp.start();
                        isPlaying = true;
                        playPauseBtn.setImageResource(R.drawable.pausebtn);
                        seekBar.setMax(mp.getDuration());
                        handler.post(updateSeekBar);
                        startImageRotation();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Previous button
                prevBtn.setOnClickListener(v -> {
                    if (songIndex > 0) {
                        songIndex--;
                        playSong(songIndex);
                    } else {
                        Toast.makeText(this, "This is the first song!", Toast.LENGTH_SHORT).show();
                    }
                });

// Next button
                nextBtn.setOnClickListener(v -> {
                    if (songIndex < songList.size() - 1) {
                        songIndex++;
                        playSong(songIndex);
                    } else {
                        Toast.makeText(this, "This is the last song!", Toast.LENGTH_SHORT).show();
                    }
                });


                // Setup rotation animator
                rotateAnimator = ObjectAnimator.ofFloat(coverImagePlayer, "rotation", 0f, 360f);
                rotateAnimator.setDuration(5000);
                rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
                rotateAnimator.setInterpolator(null);

                playPauseBtn.setOnClickListener(v -> togglePlayPause());

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
                        if (fromUser && mediaPlayer != null) {
                            mediaPlayer.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar sb) { }

                    @Override
                    public void onStopTrackingTouch(SeekBar sb) { }
                });

                mediaPlayer.setOnCompletionListener(mp -> {
                    isPlaying = false;
                    playPauseBtn.setImageResource(R.drawable.playbtn);
                    seekBar.setProgress(0);
                    rotateAnimator.end();
                });

            } else {
                // Debug if data missing
                Toast.makeText(this, "No song data received!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }




    private void togglePlayPause() {
        if (mediaPlayer == null) return;

        if (isPlaying) {
            mediaPlayer.pause();
            playPauseBtn.setImageResource(R.drawable.playbtn);
            if (rotateAnimator.isRunning()) rotateAnimator.pause();
        } else {
            mediaPlayer.start();
            playPauseBtn.setImageResource(R.drawable.pausebtn);
            handler.post(updateSeekBar);
            rotateAnimator.start();
        }
        isPlaying = !isPlaying;
    }

    private void startImageRotation() {
        if (!rotateAnimator.isRunning()) {
            rotateAnimator.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updateSeekBar);
    }

    private void playSong(int index) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        Song currentSong = songList.get(index);
        songTitle.setText(currentSong.getTitle());

        Glide.with(this)
                .load(currentSong.getImage())
                .placeholder(R.drawable.img_27)
                .into(coverImagePlayer);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(currentSong.getUrl());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                isPlaying = true;
                playPauseBtn.setImageResource(R.drawable.pausebtn);
                seekBar.setMax(mp.getDuration());
                handler.post(updateSeekBar);
                startImageRotation();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(mp -> {
            isPlaying = false;
            playPauseBtn.setImageResource(R.drawable.playbtn);
            seekBar.setProgress(0);
            rotateAnimator.end();
        });
    }



}
