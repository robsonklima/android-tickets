package com.robsonlima.tickets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        setContentView(R.layout.login_activity);
    }

    public void onLogin(View view)
    {
        progress = new ProgressDialog(this);
        progress.setTitle("Authenticating");
        progress.setMessage("Wait while authenticating...");
        progress.setCancelable(false);
        progress.show();

        postDelayed();
    }

    private void postDelayed() {
        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    progress.dismiss();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }, 1500);
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
