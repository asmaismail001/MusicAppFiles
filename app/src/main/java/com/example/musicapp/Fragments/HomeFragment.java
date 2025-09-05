package com.example.musicapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.musicapp.Adapter.SliderAdapter;
import com.example.musicapp.SongAdapter;
import com.example.musicapp.MusicPlayerActivity;
import com.example.musicapp.Model.SliderItem;
import com.example.musicapp.R;
import com.example.musicapp.Song;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewSongs;
    private ArrayList<Song> songList;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private SongAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewSongs = view.findViewById(R.id.recyclerViewSongs);
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(getContext()));

        viewPager2 = view.findViewById(R.id.slider);
        tabLayout = view.findViewById(R.id.slider_indicator);

        songList = new ArrayList<>();
        adapter = new SongAdapter(getContext(), songList);
        recyclerViewSongs.setAdapter(adapter);

        setupLocalSlider();       // Slider ke liye local drawable images
        fetchSongsFromFirebase(); // RecyclerView ke liye Firebase

        return view;
    }

    private void setupLocalSlider() {
        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.img_27, ""));
        sliderItems.add(new SliderItem(R.drawable.img_28, ""));
        sliderItems.add(new SliderItem(R.drawable.img_29, ""));
        sliderItems.add(new SliderItem(R.drawable.img_34, ""));
        sliderItems.add(new SliderItem(R.drawable.img_35, ""));

        SliderAdapter sliderAdapter = new SliderAdapter(sliderItems);
        viewPager2.setAdapter(sliderAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
        }).attach();

        // Auto-slide every 3 seconds
        viewPager2.postDelayed(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                if (i == sliderItems.size()) i = 0;
                viewPager2.setCurrentItem(i++, true);
                viewPager2.postDelayed(this, 2000);
            }
        }, 3000);
    }

    private void fetchSongsFromFirebase() {
        // Correct Firebase path for your structure
        DatabaseReference songsRef = FirebaseDatabase.getInstance()
                .getReference("Songs").child("songs");


        songsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                songList.clear();

                if (!snapshot.exists()) {
                    Log.d("FirebaseDebug", "No data found at Songs/songs");
                    return;
                }

                for (DataSnapshot songSnap : snapshot.getChildren()) {
                    String title = songSnap.child("title").getValue(String.class);
                    String artist = songSnap.child("artist").getValue(String.class);
                    String url = songSnap.child("url").getValue(String.class);
                    String image = songSnap.child("image").getValue(String.class);

                    if (title != null && artist != null && url != null && image != null) {
                        songList.add(new Song(title, artist, image, url));
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