package com.robsonlima.tickets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

import com.robsonlima.tickets.API.APIClient;
import com.robsonlima.tickets.API.APIClientInterface;
import com.robsonlima.tickets.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity {

    int postId;
    APIClientInterface apiClientInterface;
    EditText etTitle;
    EditText etBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.post_activity);

        apiClientInterface = APIClient.getClient().create(APIClientInterface.class);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etBody = (EditText) findViewById(R.id.etBody);

        if (getIntent().hasExtra("postId")) {
            postId = Integer.parseInt(getIntent().getStringExtra("postId"));
            onLoadPost();
        }
    }

    private void onLoadPost() {
        Call<List<Post>> call = apiClientInterface.getPost(postId);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.body().isEmpty()) {
                    Post post = (Post) response.body().get(0);
                    etTitle.setText(post.title);
                    etBody.setText(post.body);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                call.cancel();
            }
        });
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
