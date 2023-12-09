package com.zaad.zaad.model;

import java.util.Date;

public class Notification {

    private String notificationText;
    private Date notificationDate;

    public Notification() {
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }
}
