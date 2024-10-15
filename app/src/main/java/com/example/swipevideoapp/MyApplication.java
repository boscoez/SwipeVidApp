package com.example.swipevideoapp;

import android.app.Application;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import java.io.File;

public class MyApplication extends Application {

    private static SimpleCache simpleCache;
    private static CacheDataSource.Factory cacheDataSourceFactory;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the cache
        var databaseProvider = new ExoDatabaseProvider(this);
        var evictor = new LeastRecentlyUsedCacheEvictor(200 * 1024 * 1024); // 100 MB cache
        simpleCache = new SimpleCache(new File(getCacheDir(), "media"), evictor, databaseProvider);

        // Create the cache data source factory
        cacheDataSourceFactory = new CacheDataSource.Factory()
                .setCache(simpleCache)
                .setUpstreamDataSourceFactory(new DefaultHttpDataSource.Factory())
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
    }
    public static CacheDataSource.Factory getCacheDataSourceFactory() {
        return cacheDataSourceFactory;
    }
}

