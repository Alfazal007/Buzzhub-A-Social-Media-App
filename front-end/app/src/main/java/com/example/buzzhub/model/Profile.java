package com.example.buzzhub.model;



public class Profile {

    public String id;
    public String username;
    public int following;
    public int followers;
    public String bio;
    public ImageModel img;
    public int posts;
    public boolean followingUser;

    public Profile(String id,String username, int following, int followers, String bio,ImageModel img, int posts,boolean followingUser) {
        this.id = id;
        this.username = username;
        this.following = following;
        this.followers = followers;
        this.bio = bio;
        this.img = img;
        this.posts = posts;
        this.followingUser = followingUser;
    }
}


