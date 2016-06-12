package com.prepod.unifeed;

/**
 * Created by Антон on 10.06.2016.
 */
public class Feed {

    private String id;
    private String createdTime;
    private String stroy;
    private Long timeStamp;

    private String message; //The status message in the post.
    private String link; //The link attached to this post.
    private String caption; //The caption of a link in the post (appears beneath the name).
    private String description; //A description of a link in the post (appears beneath the caption).
    private String picture; //The picture scraped from any link included with the post.
    private String source; //A URL to any Flash movie or video file attached to the post.

    private String ownerId;

    private Person person;

    private String fromId;

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public void setStroy(String stroy) {
        this.stroy = stroy;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getId() {
        return id;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getStroy() {
        return stroy;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public String getLink() {
        return link;
    }

    public String getCaption() {
        return caption;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public String getSource() {
        return source;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Person getPerson() {
        return person;
    }

    public String getFromId() {
        return fromId;
    }
}
