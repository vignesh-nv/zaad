package com.zaad.zaad;

public enum VideoType {
    YOUTUBE_VIDEO("YOUTUBE_VIDEO"),
    INSTAGRAM_REEL("INSTAGRAM_REEL"),
    YOUTUBE_SHORTS("YOUTUBE_SHORTS"),

    IMAGE_AD("IMAGE_AD");
    String type;
    VideoType(String type) {
        this.type = type;
    }
}
