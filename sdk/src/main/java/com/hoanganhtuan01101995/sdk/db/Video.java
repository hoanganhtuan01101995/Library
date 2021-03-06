package com.hoanganhtuan01101995.sdk.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "VIDEO".
 */
@Entity
public class Video implements Serializable{

    @Id
    private String videoId;
    private String title;
    private String description;
    private Long publishedAt;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public Video() {
    }

    public Video(String videoId) {
        this.videoId = videoId;
    }

    @Generated
    public Video(String videoId, String title, String description, Long publishedAt) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Long publishedAt) {
        this.publishedAt = publishedAt;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
