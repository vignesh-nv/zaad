package com.zaad.zaad.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VideoDetails {

    @PrimaryKey @NonNull
    @ColumnInfo(name = "video_id")
    public String videoId;

    @ColumnInfo(name = "watched_seconds")
    public float watchedSeconds;

}
