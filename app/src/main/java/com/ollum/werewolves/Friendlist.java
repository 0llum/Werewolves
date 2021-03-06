package com.ollum.werewolves;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Friendlist extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    EditText searchBar;
    Button add, remove;
    UserLocalStore userLocalStore;
    User user;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendlist);

        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();
        String username_1 = userLocalStore.getLoggedInUser().username;

        BackgroundTaskRecyclerView backgroundTaskRecyclerView = new BackgroundTaskRecyclerView(Friendlist.this);
        backgroundTaskRecyclerView.execute(username_1);

        searchBar = (EditText)findViewById(R.id.friendlist_searchbar);
        add = (Button)findViewById(R.id.friendlist_add);
        add.setOnClickListener(this);
        remove = (Button)findViewById(R.id.friendlist_remove);
        remove.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.friendlist_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        setUserOnline(user);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setUserOnline(user);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setUserOffline(user);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.friendlist_add:
                String friendToAdd = searchBar.getText().toString().trim();
                addFriend(friendToAdd);
                break;
            case R.id.friendlist_remove:
                String friendToRemove = searchBar.getText().toString().trim();
                removeFriend(friendToRemove);
                break;
        }
    }

    private void addFriend(final String username_2) {
        String username_1 = userLocalStore.getLoggedInUser().username;

        String method = "addFriend";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, username_1, username_2);
    }

    private void removeFriend(final String username_2) {
        String username_1 = userLocalStore.getLoggedInUser().username;

        String method = "removeFriend";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, username_1, username_2);
    }

    private void setUserOnline(User user) {
        String method = "online";
        BackgroundTaskStatus backgroundTaskStatus = new BackgroundTaskStatus(this);
        backgroundTaskStatus.execute(method, user.username);
    }

    private void setUserOffline(User user) {
        String method = "offline";
        BackgroundTaskStatus backgroundTaskStatus = new BackgroundTaskStatus(this);
        backgroundTaskStatus.execute(method, user.username);
    }

    private void setUserAFK(User user) {
        String method = "afk";
        BackgroundTaskStatus backgroundTaskStatus = new BackgroundTaskStatus(this);
        backgroundTaskStatus.execute(method, user.username);
    }

    @Override
    public void onRefresh() {
        finish();
        startActivity(new Intent(this, Friendlist.class));
        overridePendingTransition(0, 0);
    }
}
