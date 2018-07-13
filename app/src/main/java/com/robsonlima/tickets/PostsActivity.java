package com.robsonlima.tickets;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.robsonlima.tickets.API.APIClient;
import com.robsonlima.tickets.interfaces.PostInterface;
import com.robsonlima.tickets.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsActivity extends AppCompatActivity {

    PostInterface postInterface;
    List<Post> postList;
    ListView listPosts;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.posts_activity);

        postInterface = APIClient.getClient().create(PostInterface.class);
        listPosts = (ListView) findViewById(R.id.listPosts);

        onLoadPosts();

        listPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Post post = (Post) parent.getItemAtPosition(pos);
                Intent intent = new Intent(PostsActivity.this, PostActivity.class);
                intent.putExtra("postId", post.id.toString());
                startActivity(intent);
            }
        });

        listPosts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                Post post = (Post) parent.getItemAtPosition(pos);
                deletePost(post);

                return true;
            }
        });
    }

    private void deletePost(final Post post) {
        new AlertDialog.Builder(this)
            .setTitle("Confirm")
            .setMessage("Do you really want to delete this post?")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    progress = new ProgressDialog(PostsActivity.this);
                    progress.setTitle("Loading");
                    progress.setMessage("Wait while loading...");
                    progress.setCancelable(false);
                    progress.show();

                    Call<Post> call = postInterface.deletePost(post.id);
                    call.enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(Call<Post> call, Response<Post> response) {
                            Snackbar.make(findViewById(R.id.postsActivity),post.title + " deleted!",
                                    Snackbar.LENGTH_LONG).show();

                            progress.dismiss();
                        }

                        @Override
                        public void onFailure(Call<Post> call, Throwable t) {
                            call.cancel();

                            progress.dismiss();
                        }
                    });
                }})
            .setNegativeButton(android.R.string.no, null).show();
    }

    private void onLoadPosts() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();

        Call<List<Post>> call = postInterface.getListPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                postList = response.body();
                onLoadListPosts();
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Snackbar.make(findViewById(R.id.postsActivity), "Error on getting data", Snackbar.LENGTH_LONG).show();
                call.cancel();
                progress.dismiss();
                finish();
            }
        });
    }

    private void onLoadListPosts() {
        ArrayAdapter<Post> adapter = new ArrayAdapter<Post>(PostsActivity.this, android.R.layout.simple_list_item_1, postList);
        listPosts.setAdapter(adapter);
    }

    public void onCreatePost(View view) {
        Intent intent = new Intent(PostsActivity.this, PostActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}