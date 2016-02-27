package com.ollum.werewolves;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    Button bFriends, bStats, bRules, bOpenGames, bCreateGame, bLogout;
    UserLocalStore userLocalStore;

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

        bFriends.setOnClickListener(this);
        bStats.setOnClickListener(this);
        bRules.setOnClickListener(this);
        bOpenGames.setOnClickListener(this);
        bCreateGame.setOnClickListener(this);
        bLogout.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if(!authenticate()) {
            startActivity(new Intent(Menu.this, Login.class));
            overridePendingTransition(0, 0);
        }*/
    }

    /*private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_friends:
                startActivity(new Intent(this, Friendlist.class));
                overridePendingTransition(0, 0);

                //displayFriends();

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
                //BackgroundTask.isLoggedIn = false;
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, Login.class));
                overridePendingTransition(0, 0);
                break;
        }
    }

    private void displayFriends() {
        String method = "displayFriends";
        BackgroundTaskRecyclerView backgroundTaskRecyclerView = new BackgroundTaskRecyclerView(this);
        backgroundTaskRecyclerView.execute();
    }
}
