package com.zaad.zaad.model;

import java.util.List;

public class ShoppingMenu {

    private String availability;

    private List<ShoppingMenuItem> shoppingMenuItemList;

    private String title;

    private String imageUrl;

    private String link;

    public ShoppingMenu() {
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public List<ShoppingMenuItem> getShoppingMenuItemList() {
        return shoppingMenuItemList;
    }

    public void setShoppingMenuItemList(List<ShoppingMenuItem> shoppingMenuItemList) {
        this.shoppingMenuItemList = shoppingMenuItemList;
    }

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
