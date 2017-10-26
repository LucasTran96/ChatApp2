package com.huytranse.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by HuyTranSE on 10/16/2017.
 */
@IgnoreExtraProperties
public class User {
    private String email;

    public User() {
        // lưu giá trị từ firebase về
    }

    public User(String email) {
        this.email = email;
        // set giá trị từ object ra app
    }
}
