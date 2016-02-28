package com.ollum.werewolves;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Rules extends AppCompatActivity {

    UserLocalStore userLocalStore;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules);

        user = userLocalStore.getLoggedInUser();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setUserAFK(user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserOnline(user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setUserOffline(user);
    }

    private void setUserOnline(User user) {
        String method = "online";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, user.username);
    }

    private void setUserOffline(User user) {
        String method = "offline";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, user.username);
    }

    private void setUserAFK(User user) {
        String method = "afk";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, user.username);
    }
}
