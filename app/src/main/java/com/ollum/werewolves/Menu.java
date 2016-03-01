package com.ollum.werewolves;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Service;
import android.widget.Toast;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    TextView welcome;
    Button bFriends, bStats, bRules, bOpenGames, bCreateGame, bLogout, bSettings;
    UserLocalStore userLocalStore;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        bFriends = (Button)findViewById(R.id.menu_friends);
        bStats = (Button)findViewById(R.id.menu_stats);
        bRules = (Button)findViewById(R.id.menu_rules);
        bOpenGames = (Button)findViewById(R.id.menu_openGames);
        bCreateGame = (Button)findViewById(R.id.menu_createGame);
        bLogout = (Button)findViewById(R.id.menu_logout);
        bSettings = (Button)findViewById(R.id.menu_settings);

        bFriends.setOnClickListener(this);
        bStats.setOnClickListener(this);
        bRules.setOnClickListener(this);
        bOpenGames.setOnClickListener(this);
        bCreateGame.setOnClickListener(this);
        bLogout.setOnClickListener(this);
        bSettings.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();

        setUserOnline(user);

        welcome = (TextView)findViewById(R.id.menu_welcome);
        welcome.setText("Welcome, " + user.getUsername());
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
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_friends:
                startActivity(new Intent(this, Friendlist.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.menu_stats:
                break;
            case R.id.menu_rules:
                startActivity(new Intent(this, Rules.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.menu_openGames:
                break;
            case R.id.menu_createGame:
                break;
            case R.id.menu_logout:
                setUserOffline(user);
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

                finish();
                startActivity(new Intent(this, Login.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.menu_settings:
                startActivity(new Intent(this, Settings.class));
                overridePendingTransition(0, 0);
                break;
        }
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
}
