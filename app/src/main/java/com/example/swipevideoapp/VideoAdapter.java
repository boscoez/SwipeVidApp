package com.example.swipevideoapp;

import static com.google.android.exoplayer2.Player.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private final List<VideoItem> videoItems;

    public VideoAdapter(List<VideoItem> videoItems) {
        this.videoItems = videoItems;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_video, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.setVideoData(videoItems.get(position));
    }

    @Override
    public void onViewRecycled(@NonNull VideoViewHolder holder) {
        super.onViewRecycled(holder);
        holder.releasePlayer();
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView textVideoTitle1, textVideoDescription1;
        PlayerView playerView;
        ProgressBar progressBar;
        SimpleExoPlayer player;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.playerView);
            textVideoTitle1 = itemView.findViewById(R.id.textVideoTitle);
            textVideoDescription1 = itemView.findViewById(R.id.textVideoDescription);
            progressBar = itemView.findViewById(R.id.videoProgressBar);
        }

        void setVideoData(VideoItem videoItem) {
            textVideoTitle1.setText(videoItem.videoTitle);
            textVideoDescription1.setText(videoItem.videoDescription);

            // Show the progress bar until the player is ready
            progressBar.setVisibility(View.VISIBLE);

            // Initialize ExoPlayer on the main thread
            LoadControl loadControl = new DefaultLoadControl.Builder()
                    .setBufferDurationsMs(
                            DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                            DefaultLoadControl.DEFAULT_MAX_BUFFER_MS * 2,
                            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
                    )
                    .build();

            player = new SimpleExoPlayer.Builder(itemView.getContext())
                    .setLoadControl(loadControl)
                    .build();

            // Use the cacheDataSourceFactory
            DataSource.Factory dataSourceFactory = MyApplication.getCacheDataSourceFactory();

            // Create MediaSource
            var mediaItem = MediaItem.fromUri(videoItem.videoURL);
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem);

            // Set the media source and prepare the player on the main thread
            playerView.post(() -> {
                player.setMediaSource(mediaSource);
                player.prepare();
                player.setPlayWhenReady(true);

                // Add player listener
                player.addListener(new Listener() {
                    @Override
                    public void onPlaybackStateChanged(int state) {
                        if (state == STATE_READY) {
                            progressBar.setVisibility(View.GONE);
                        } else if (state == STATE_BUFFERING) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onPlayerError(@NonNull PlaybackException error) {
                        Log.e("ExoPlayer", "Error: " + error.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                });

                // Set the player to the PlayerView
                playerView.setPlayer(player);
            });
        }

        void releasePlayer() {
            if (player != null) {
                player.release();
                player = null;
            }
        }
    }
}
