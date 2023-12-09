package com.zaad.zaad.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Video implements Comparable, Serializable {
    private String title;
    private String imageUrl;
    private String videoUrl;
    private String category;

    private String id;

    private Date uploadDate;

    private String language;

    private List<String> adIds;

    private String channelUrl;

    private String fullVideoUrl;

    private String bannerId;

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


    public List<String> getAdIds() {
        return adIds;
    }

    public void setAdIds(List<String> adIds) {
        this.adIds = adIds;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getFullVideoUrl() {
        return fullVideoUrl;
    }

    public void setFullVideoUrl(String fullVideoUrl) {
        this.fullVideoUrl = fullVideoUrl;
    }

    @Override
    public String toString() {
        return "Video{" +
                "title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", category='" + category + '\'' +
                ", id='" + id + '\'' +
                ", uploadDate=" + uploadDate +
                ", language='" + language + '\'' +
                ", adIds=" + adIds +
                ", channelUrl='" + channelUrl + '\'' +
                ", fullVideoUrl='" + fullVideoUrl + '\'' +
                ", bannerId='" + bannerId + '\'' +
                '}';
    }


    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    @Override
    public int compareTo(Object o) {
        Date uploadDate = ((Video)o).getUploadDate();
        return uploadDate.compareTo(this.uploadDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(id, video.id);
    }
}
