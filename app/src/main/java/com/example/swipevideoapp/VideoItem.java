package com.example.swipevideoapp;

public class VideoItem {
    public String videoURL;
    public String videoTitle;
    public String videoDescription;

    // Constructor to initialize all fields
    public VideoItem(String videoURL, String videoTitle, String videoDescription) {
        // If a videoID is provided, use it, otherwise generate a new UUID
        this.videoURL = videoURL;
        this.videoTitle = videoTitle;
        this.videoDescription = videoDescription;
    }
}
