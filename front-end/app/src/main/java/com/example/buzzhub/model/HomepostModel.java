package com.example.buzzhub.model;

public class HomepostModel {

    int profileimg,postimg,bookmarkimg;
    String username,about,like,comment,share,caption;

    public HomepostModel(int profileimg, int postimg, String username, String about, String like, String comment, String share, String caption) {
        this.profileimg = profileimg;
        this.postimg = postimg;
        this.bookmarkimg = bookmarkimg;
        this.username = username;
        this.about = about;
        this.like = like;
        this.comment = comment;
        this.share = share;
        this.caption = caption;
    }

    public int getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(int profileimg) {
        this.profileimg = profileimg;
    }

    public int getPostimg() {
        return postimg;
    }

    public void setPostimg(int postimg) {
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
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
