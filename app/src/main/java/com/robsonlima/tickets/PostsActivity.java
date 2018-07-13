package com.robsonlima.tickets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.robsonlima.tickets.API.APIClient;
import com.robsonlima.tickets.API.APIClientInterface;
import com.robsonlima.tickets.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsActivity extends AppCompatActivity {

    APIClientInterface apiClientInterface;
    List<Post> postList;
    ListView listPosts;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.posts_activity);

        apiClientInterface = APIClient.getClient().create(APIClientInterface.class);
        listPosts = (ListView) findViewById(R.id.listPosts);

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.show();

        Call<List<Post>> call = apiClientInterface.getListPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                postList = response.body();
                onLoadListPosts();
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                progress.dismiss();
                call.cancel();
            }
        });

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = (Post) parent.getItemAtPosition(position);
                Intent intent = new Intent(PostsActivity.this, PostActivity.class);
                intent.putExtra("postId", post.id.toString());
                startActivity(intent);
            }
        };
        listPosts.setOnItemClickListener(listener);
    }

    private void onLoadListPosts() {
        ArrayAdapter<Post> adapter = new ArrayAdapter<Post>(PostsActivity.this, android.R.layout.simple_list_item_1, postList);
        listPosts.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}