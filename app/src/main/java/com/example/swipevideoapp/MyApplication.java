package com.example.swipevideoapp;

import android.app.Application;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import java.io.File;
/**
 * Custom Application class to manage the initialization of the media cache.
 * This class sets up ExoPlayer's cache for media playback and provides a global instance of
 * the CacheDataSource.Factory for use across the app.
 */
public class MyApplication extends Application {
    /** A global instance of the SimpleCache used to store media files locally. */
    private static SimpleCache simpleCache;
    /** A factory for creating cache-enabled data sources for media streaming. */
    private static CacheDataSource.Factory cacheDataSourceFactory;
    /**
     * Called when the application is created. Initializes the media cache and
     * sets up the CacheDataSource.Factory used by ExoPlayer for media streaming.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // Create the ExoPlayer database provider for managing cached data.
        var databaseProvider = new ExoDatabaseProvider(this);
        // Set up an eviction policy that removes the least recently used data when the cache exceeds 200MB.
        var evictor = new LeastRecentlyUsedCacheEvictor(200 * 1024 * 1024); // 200 MB cache limit
        // Initialize SimpleCache with the app's cache directory, eviction policy, and database provider.
        simpleCache = new SimpleCache(new File(getCacheDir(), "media"), evictor, databaseProvider);
        // Set up a CacheDataSource.Factory, which combines cached and network data sources.
        cacheDataSourceFactory = new CacheDataSource.Factory()
                .setCache(simpleCache)
                .setUpstreamDataSourceFactory(new DefaultHttpDataSource.Factory()) // HTTP data source for network requests
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR); // Ignore cache errors during media playback
    }
    /**
     * Provides a global instance of CacheDataSource.Factory for creating cache-enabled data sources.
     * @return A CacheDataSource.Factory instance to be used by ExoPlayer for streaming media.
     */
    public static CacheDataSource.Factory getCacheDataSourceFactory() {
        return cacheDataSourceFactory;
    }
}
