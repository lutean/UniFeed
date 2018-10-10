package com.prepod.unifeed.models;

public class Attachment{

    String photo;
    String text;
    String description;
    String title;
    String url;
    String imageSrc;

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getPhoto() {
        return photo;
    }

    public String getText() {
        return text;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getImageSrc() {
        return imageSrc;
    }
}
