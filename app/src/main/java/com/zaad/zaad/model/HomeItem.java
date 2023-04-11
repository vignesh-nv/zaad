package com.zaad.zaad.model;

import java.util.List;

public class HomeItem implements Comparable {
    private String title;
    private List<Video> videos;
    private String category;

    private String collection;
    private int order;
    private boolean showVideoCategory;
    private String videoCategory;

    private List<String> categoryFilter;

    boolean languageFilter;

    public HomeItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isShowVideoCategory() {
        return showVideoCategory;
    }

    public void setShowVideoCategory(boolean showVideoCategory) {
        this.showVideoCategory = showVideoCategory;
    }

    public String getVideoCategory() {
        return videoCategory;
    }

    public void setVideoCategory(String videoCategory) {
        this.videoCategory = videoCategory;
    }

    public boolean isLanguageFilter() {
        return languageFilter;
    }

    public void setLanguageFilter(boolean languageFilter) {
        this.languageFilter = languageFilter;
    }

    public List<String> getCategoryFilter() {
        return categoryFilter;
    }

    public void setCategoryFilter(List<String> categoryFilter) {
        this.categoryFilter = categoryFilter;
    }

    @Override
    public String toString() {
        return "HomeItem{" +
                "title='" + title + '\'' +
                ", videos=" + videos +
                ", category='" + category + '\'' +
                ", collection='" + collection + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        int compareOrder
                = ((HomeItem)o).getOrder();

        return this.order - compareOrder;
    }
}
