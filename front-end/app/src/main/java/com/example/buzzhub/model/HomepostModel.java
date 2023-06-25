package com.example.buzzhub.model;


import com.google.gson.annotations.SerializedName;

// hi
public class HomepostModel {

    @SerializedName("profilePic")
    ImageModel profileimg;

    @SerializedName("img")
    ImageModel postimg;
    String username,about,like,comment,share,description;

    public HomepostModel(ImageModel profileimg, ImageModel postimg, String username, String about, String like, String comment, String share, String caption) {
        this.profileimg = profileimg;
        this.postimg = postimg;
        this.username = username;
        this.about = about;
        this.like = like;
        this.comment = comment;
        this.share = share;
        this.description = caption;
    }

    public ImageModel getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(ImageModel profileimg) {
        this.profileimg = profileimg;
    }

    public ImageModel getPostimg() {
        return postimg;
    }

    public void setPostimg(ImageModel postimg) {
        this.postimg = postimg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getCaption() {
        return description;
    }

    public void setCaption(String caption) {
        this.description = caption;
    }
}
