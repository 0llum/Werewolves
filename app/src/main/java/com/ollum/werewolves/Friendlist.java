package com.ollum.werewolves;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Friendlist extends AppCompatActivity implements View.OnClickListener {

    ListView list;
    EditText searchBar;
    Button add;
    UserLocalStore userLocalStore;

    InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendlist);

        userLocalStore = new UserLocalStore(this);
        String username_1 = userLocalStore.getLoggedInUser().username;

        BackgroundTaskRecyclerView backgroundTaskRecyclerView = new BackgroundTaskRecyclerView(Friendlist.this);
        backgroundTaskRecyclerView.execute(username_1);

        //list = (ListView)findViewById(R.id.friendlist_listView);
        searchBar = (EditText)findViewById(R.id.friendlist_searchbar);
        add = (Button)findViewById(R.id.friendlist_add);

        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.friendlist_add:
                String username_2 = searchBar.getText().toString().trim();
                addFriend(username_2);
                break;
        }
    }

    private void addFriend(final String username_2) {
        String username_1 = userLocalStore.getLoggedInUser().username;

        String method = "addFriend";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, username_1, username_2);
    }
}
