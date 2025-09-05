package com.example.musicapp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SongsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSongs;
    private ArrayList<Song> songList;
    private SongAdapter adapter;
    private DatabaseReference songsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        recyclerViewSongs = findViewById(R.id.recyclerViewSongs);
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(this));

        songList = new ArrayList<>();
        adapter = new SongAdapter(this, songList);
        recyclerViewSongs.setAdapter(adapter);

        songsRef = FirebaseDatabase.getInstance().getReference("Songs/songs");

        loadSongsFromFirebase();
    }

    private void loadSongsFromFirebase() {
        songsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                songList.clear();

                if (!snapshot.exists()) {
                    Log.d("FirebaseDebug", "No data found at Songs/songs");
                    return;
                }

                for (DataSnapshot songSnapshot : snapshot.getChildren()) {
                    String title = songSnapshot.child("title").getValue(String.class);
                    String artist = songSnapshot.child("artist").getValue(String.class);
                    String url = songSnapshot.child("url").getValue(String.class);
                    String image = songSnapshot.child("image").getValue(String.class);

                    if (title != null && artist != null && url != null && image != null) {
                        songList.add(new Song(title, artist, url, image));
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseDebug", "Error: " + error.getMessage());
            }
        });
    }
}