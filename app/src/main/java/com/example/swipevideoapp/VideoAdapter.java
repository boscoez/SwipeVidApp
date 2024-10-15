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

/**
 * Adapter for binding video items to a RecyclerView and handling media playback using ExoPlayer.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    /** List of video items to be displayed and played in the RecyclerView. */
    private final List<VideoItem> videoItems;

    /**
     * Constructor for VideoAdapter.
     *
     * @param videoItems List of VideoItem objects to be bound to the RecyclerView.
     */
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
        holder.setVideoData(videoItems.get(position));  // Bind video data for the current item
    }

    @Override
    public void onViewRecycled(@NonNull VideoViewHolder holder) {
        super.onViewRecycled(holder);
        holder.releasePlayer();  // Release player resources when the view is recycled
    }

    @Override
    public int getItemCount() {
        return videoItems.size();  // Return the number of video items in the list
    }

    /**
     * ViewHolder class that represents each video item in the RecyclerView and manages its ExoPlayer instance.
     */
    static class VideoViewHolder extends RecyclerView.ViewHolder {
        /** TextView for displaying the video title and the video description. */
        TextView textVideoTitle1, textVideoDescription1;
        /** ExoPlayer's PlayerView used to play the video. */
        PlayerView playerView;
        /** ProgressBar shown while the video is buffering or loading. */
        ProgressBar progressBar;
        /** Instance of ExoPlayer used to manage media playback. */
        SimpleExoPlayer player;
        /**
         * Constructor for VideoViewHolder. Initializes the view elements for each video item.
         * @param itemView The view corresponding to a single video item in the RecyclerView.
         */
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.playerView);
            textVideoTitle1 = itemView.findViewById(R.id.textVideoTitle);
            textVideoDescription1 = itemView.findViewById(R.id.textVideoDescription);
            progressBar = itemView.findViewById(R.id.videoProgressBar);
        }
        /**
         * Binds the video data to the view and sets up the ExoPlayer for playback.
         * @param videoItem The video item containing the video URL, title, and description.
         */
        void setVideoData(VideoItem videoItem) {
            textVideoTitle1.setText(videoItem.videoTitle);  // Set video title
            textVideoDescription1.setText(videoItem.videoDescription);  // Set video description
            // Show the progress bar until the player is ready
            progressBar.setVisibility(View.VISIBLE);
            // Initialize ExoPlayer with custom load control to handle buffer settings
            LoadControl loadControl = new DefaultLoadControl.Builder()
                    .setBufferDurationsMs(
                            DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                            DefaultLoadControl.DEFAULT_MAX_BUFFER_MS * 2,  // Increase max buffer size
                            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
                    )
                    .build();
            player = new SimpleExoPlayer.Builder(itemView.getContext())
                    .setLoadControl(loadControl)
                    .build();
            // Use the cached data source factory for efficient media loading
            DataSource.Factory dataSourceFactory = MyApplication.getCacheDataSourceFactory();
            // Create a MediaItem and MediaSource for the video URL
            var mediaItem = MediaItem.fromUri(videoItem.videoURL);
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem);
            // Prepare and play the media using ExoPlayer
            playerView.post(() -> {
                player.setMediaSource(mediaSource);
                player.prepare();
                player.setPlayWhenReady(true);  // Automatically start playback when ready
                // Listen for playback state changes to control the visibility of the progress bar
                player.addListener(new Listener() {
                    @Override
                    public void onPlaybackStateChanged(int state) {
                        if (state == STATE_READY) {
                            progressBar.setVisibility(View.GONE);  // Hide progress bar when ready to play
                        } else if (state == STATE_BUFFERING) {
                            progressBar.setVisibility(View.VISIBLE);  // Show progress bar while buffering
                        }
                    }
                    @Override
                    public void onPlayerError(@NonNull PlaybackException error) {
                        Log.e("ExoPlayer", "Error: " + error.getMessage());  // Log any playback errors
                        progressBar.setVisibility(View.GONE);  // Hide progress bar on error
                    }
                });
                // Attach the player to the PlayerView
                playerView.setPlayer(player);
            });
        }
        /**
         * Releases the ExoPlayer when the view is no longer in use to free up resources.
         */
        void releasePlayer() {
            if (player != null) {
                player.release();  // Release ExoPlayer resources
                player = null;
            }
        }
    }
}
