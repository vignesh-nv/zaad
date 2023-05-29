package com.zaad.zaad.cache;

import android.content.Context;

import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

public class AppCache {
    public static SimpleCache simpleCache;

    public static SimpleCache getSimpleCache(Context context) {
        if (simpleCache == null) {
            LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(90 * 1024 * 1024);
            DatabaseProvider databaseProvider = new StandaloneDatabaseProvider(context);
            simpleCache = new SimpleCache(context.getCacheDir(), leastRecentlyUsedCacheEvictor, databaseProvider);
        }
        return simpleCache;
    }

}
