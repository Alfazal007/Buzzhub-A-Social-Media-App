package com.example.buzzhub.model;

public class ImageModel {
    public String type;
    public byte[] data;

    public ImageModel(String type, byte[] data) {
        this.type = type;
        this.data = data;
    }
}
