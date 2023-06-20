package com.example.buzzhub.model;

public class UpdateEmailAndPassword {
    private String email;
    private String old_password;
    private String new_password;

    public UpdateEmailAndPassword(String email, String old_password,String new_password) {
        this.email = email;
        this.old_password = old_password;
        this.new_password = new_password;
    }
}
