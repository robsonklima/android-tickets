package com.robsonlima.tickets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.robsonlima.tickets.API.APIClient;
import com.robsonlima.tickets.interfaces.PostInterface;
import com.robsonlima.tickets.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity {

    int postId;
    Post post;
    PostInterface postInterface;
    EditText etTitle;
    EditText etBody;
    Button btnSubmit;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.post_activity);

        postInterface = APIClient.getClient().create(PostInterface.class);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etBody = (EditText) findViewById(R.id.etBody);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        if (getIntent().hasExtra("postId")) {
            postId = Integer.parseInt(getIntent().getStringExtra("postId"));
            onLoadPost();
        }
    }

    private void onLoadPost() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();

        Call<List<Post>> call = postInterface.getPost(postId);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.body().isEmpty()) {
                    post = (Post) response.body().get(0);
                    etTitle.setText(post.title);
                    etBody.setText(post.body);
                    progress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Snackbar.make(findViewById(R.id.postActivity), "Error on getting data",
                        Snackbar.LENGTH_LONG).show();
                call.cancel();
                progress.dismiss();
                finish();
            }
        });
    }

    public void onClickSubmit(View view) {
        String title = etTitle.getText().toString();
        String body = etBody.getText().toString();

        if (title.isEmpty() || title.length() < 4) {
            etTitle.setError("at least 4 alphanumeric characters");
            return;
        }

        if (body.isEmpty() || body.length() < 4 || body.length() > 250) {
            etBody.setError("between 4 and 250 alphanumeric characters");
            return;
        }

        if (postId == 0) {
            Post newPost = new Post(title, body);
            createPost(newPost);
        } else {
            updatePost(post);
        }

        closeKeyboard(view);
    }

    private void createPost(Post post) {
        progress = new ProgressDialog(this);
        progress.setTitle("Processing");
        progress.setMessage("Wait while creating the post...");
        progress.setCancelable(false);
        progress.show();

        Call<Post> call = postInterface.createPost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Post post = response.body();

                Snackbar.make(findViewById(R.id.postActivity), post.title + " created!",
                        Snackbar.LENGTH_LONG).show();

                progress.dismiss();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                call.cancel();

                progress.dismiss();
            }
        });
    }

    private void updatePost(Post post) {
        progress = new ProgressDialog(this);
        progress.setTitle("Processing");
        progress.setMessage("Wait while updating the post...");
        progress.setCancelable(false);
        progress.show();

        Call<Post> call = postInterface.updatePost(post.id, post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Post post = response.body();

                Snackbar.make(findViewById(R.id.postActivity), post.title + " updated!",
                        Snackbar.LENGTH_LONG).show();

                progress.dismiss();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                call.cancel();

                progress.dismiss();
            }
        });
    }

    private void closeKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
