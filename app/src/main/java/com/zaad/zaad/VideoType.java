package com.zaad.zaad;

public enum VideoType {
    YOUTUBE_VIDEO("YOUTUBE_VIDEO"),
    INSTAGRAM_REEL("INSTAGRAM_REEL"),
    YOUTUBE_SHORTS("YOUTUBE_SHORTS"),

    FACEBOOK_VIDEOS("FACEBOOK_VIDEOS"),

    IMAGE_AD("IMAGE_AD");
    String type;
    VideoType(String type) {
        this.type = type;
    }
}
