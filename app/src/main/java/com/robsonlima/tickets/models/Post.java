package com.robsonlima.tickets.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("userId")
    @Expose
    public Integer userId;

    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("body")
    @Expose
    public String body;

    @Override
    public String toString() {
        return this.title;
    }
}