package com.zaad.zaad.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VideoDetailsDao {

    @Query("SELECT watched_seconds FROM videodetails WHERE video_id=(:videoId)")
    LiveData<Float> getWatchedSeconds(String videoId);

    @Update
    void updateWatchedSeconds(VideoDetails... videoDetails);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideoDetails(VideoDetails videoDetails);
}
