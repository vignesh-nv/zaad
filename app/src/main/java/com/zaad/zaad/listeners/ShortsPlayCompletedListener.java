package com.zaad.zaad.listeners;

import com.zaad.zaad.model.DailyTaskVideo;
import com.zaad.zaad.model.Video;

public interface ShortsPlayCompletedListener {
    void onCompleted(DailyTaskVideo video);
}
