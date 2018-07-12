package com.robsonlima.tickets.API;

import com.robsonlima.tickets.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIClientInterface {

    @GET("/posts")
    Call<List<Post>> getListPosts();

    @GET("/posts/?")
    Call<List<Post>> getPost(@Query("id") int id);

}

