package com.robsonlima.tickets.interfaces;

import com.robsonlima.tickets.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostInterface {

    @GET("/posts")
    Call<List<Post>> getListPosts();

    @GET("/posts/?")
    Call<List<Post>> getPost(@Query("id") int id);

    @POST("/posts/")
    Call<Post> createPost(@Body Post post);

    @PUT("/posts/{id}")
    Call <Post> updatePost(@Path("id") int id, @Body Post post);

    @DELETE("/posts/{id}")
    Call <Post> deletePost(@Path("id") int id);
}
