package com.zaad.zaad.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Video implements Comparable, Serializable {
    private String title;
    private String imageUrl;
    private String videoUrl;
    private String category;

    private String id;

    private Date uploadDate;

    private String language;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "Video{" +
                "title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Date uploadDate = ((Video)o).getUploadDate();
        return uploadDate.compareTo(this.uploadDate);
    }

}
