package com.hoanganhtuan01101995.sdk.db;

import java.io.Serializable;

/**
 * Created by HoangAnhTuan on 8/29/2017.
 */

public class Playlist implements Serializable {
    private String id;
    private String title;
    private String description;
    private String medium;
    private int localImage;
    private long publishedAt;
    private boolean select;

    public Playlist() {
    }

    public Playlist(String id) {
        this.id = id;
    }

    public Playlist(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public Playlist(String id, String title, String description, String medium, int localImage, long publishedAt, boolean select) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.medium = medium;
        this.localImage = localImage;
        this.publishedAt = publishedAt;
        this.select = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLocalImage() {
        return localImage;
    }

    public void setLocalImage(int localImage) {
        this.localImage = localImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public long getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(long publishedAt) {
        this.publishedAt = publishedAt;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }


}
