package com.example.swipevideoapp;
/**
 * Represents a video item with its URL, title, and description.
 * This class is used to encapsulate the data for a video in the swipe video application.
 */
public class VideoItem {
    /** The URL of the video. */
    public String videoURL;
    /** The title of the video. */
    public String videoTitle;
    /** The description of the video. */
    public String videoDescription;
    /**
     * Constructor to initialize all fields of the VideoItem.
     * @param videoURL The URL where the video is hosted.
     * @param videoTitle The title of the video.
     * @param videoDescription A short description of the video.
     */
    public VideoItem(String videoURL, String videoTitle, String videoDescription) {
        // Initialize the video item fields with the provided values.
        this.videoURL = videoURL;
        this.videoTitle = videoTitle;
        this.videoDescription = videoDescription;
    }
}
