package com.example.swipevideoapp;

import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private void hideSystemUI() {
        // Enables immersive mode, hides system bars and makes the content fullscreen
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // Hide the nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN); // Hide the status bar
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        hideSystemUI();
    }
        public void onWindowFocusChanged(boolean hasFocus) {
            super.onWindowFocusChanged(hasFocus);
            if (hasFocus) {
                hideSystemUI(); // Reapply immersive mode when the window regains focus
            }

            // Initialize ViewPager2 and Video Adapter
            final ViewPager2 videosViewPager = findViewById(R.id.videosViewPager);

            // Create a list of videos to display
            List<VideoItem> videoItems = new ArrayList<>();

            // Add video items with unique IDs
            videoItems.add(new VideoItem(

                    "https://firebasestorage.googleapis.com/v0/b/fir-connect-2c530.appspot.com/o/4434143-hd_1080_1920_30fps.mp4?alt=media&token=24d30b50-b7d5-44da-b2e4-15df079516e9",
                    "Exploring The Wild",
                    "Embark on an adventurous journey to the sea.       -Video ID:0001"
            ));

            videoItems.add(new VideoItem(
                    "https://firebasestorage.googleapis.com/v0/b/fir-connect-2c530.appspot.com/o/6040389-hd_1080_1920_30fps.mp4?alt=media&token=ba1acdc2-55c5-41fd-b046-45889fb2287d",
                    "Cutest Koala Bear",
                    "Just a bear and her eucalyptus tree.       -Video ID:0002"
            ));

            videoItems.add(new VideoItem(
                    "https://firebasestorage.googleapis.com/v0/b/fir-connect-2c530.appspot.com/o/15548687-hd_1080_1920_60fps.mp4?alt=media&token=5c9fd7b5-1b0a-4736-ab10-96d4ddbc0bac",
                    "The Bald Eagle",
                    "He who flies the highest!      -Video ID:0003"
            ));

// Set the adapter once, after creating all video items
            videosViewPager.setAdapter(new VideoAdapter(videoItems));
        }
}
