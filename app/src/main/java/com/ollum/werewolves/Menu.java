package com.ollum.werewolves;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Service;
import android.widget.Toast;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;

import java.util.HashMap;
import java.util.Map;

public class Menu extends AppCompatActivity implements View.OnClickListener, ConnectionRequestListener, ZoneRequestListener, RoomRequestListener {

    TextView welcome;
    Button bFriends, bStats, bRules, bOpenGames, bCreateGame, bLogout, bSettings;
    UserLocalStore userLocalStore;
    User user;

    private WarpClient theClient;
    private ProgressDialog progressDialog;
    HashMap<String, Object> propertiesToMatch;
    private String roomIdJoined = "";
    private String[] roomIds;
    private int roomIdCounter = 0;

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
    protected void onStart() {
        super.onStart();
        //theClient.addZoneRequestListener(this);
        //theClient.addRoomRequestListener(this);
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
    protected void onStop() {
        super.onStop();
        //theClient.removeZoneRequestListener(this);
        //theClient.removeRoomRequestListener(this);
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
                //startActivity(new Intent(this, MainActivity.class));
                //overridePendingTransition(0, 0);

                init();

                String userName = user.username;

                Utils.USER_NAME  = userName;
                progressDialog = ProgressDialog.show(this, "", "Please wait...");
                progressDialog.setCancelable(false);
                theClient.addConnectionRequestListener(this);
                theClient.connectWithUserName(userName);

                theClient.addZoneRequestListener(this);
                theClient.addRoomRequestListener(this);

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

    private void init(){
        WarpClient.initialize(Constants.apiKey, Constants.secretKey);
        try {
            theClient = WarpClient.getInstance();
            WarpClient.enableTrace(true);
        } catch (Exception ex) {
            Toast.makeText(this, "Exception in Initilization", Toast.LENGTH_LONG).show();
        }
        theClient.addConnectionRequestListener(this);
    }

    @Override
    public void onConnectDone(final ConnectEvent event) {
        Log.d("OnConnectDone", "" + event.getResult());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                if (event.getResult() == WarpResponseResultCode.SUCCESS) {
                    Toast.makeText(Menu.this, "Connection Success", Toast.LENGTH_SHORT).show();
                    joinChatroom();
                } else {
                    Toast.makeText(Menu.this, "Connection Failed" + event.getResult(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void joinChatroom() {
        roomIdCounter = 0;
        roomIds = null;
        roomIdJoined = "";

        if(propertiesToMatch==null){
            propertiesToMatch = new HashMap<String, Object>();
        }else{
            propertiesToMatch.clear();
        }
        propertiesToMatch.put("topic", "chatroom");

        theClient.joinRoomWithProperties(propertiesToMatch);
    }

    @Override
    public void onJoinRoomDone(final RoomEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (event.getResult() == WarpResponseResultCode.SUCCESS) {
                    roomIdJoined = event.getData().getId();
                    joinChat();
                } else {
                }
            }
        });
    }

    public void joinChat() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("roomId", roomIdJoined);
        startActivity(intent);
    }

    @Override
    public void onGetLiveRoomInfoDone(final LiveRoomInfoEvent event) {
        HashMap roomProperties = event.getProperties();
        Log.d("roomProperties" + roomProperties, "propertiesToMatch" + propertiesToMatch);

        theClient.joinRoom("" + event.getData().getId());
    }

    @Override
    public void onGetAllRoomsDone(AllRoomsEvent event) {
        roomIds = event.getRoomIds();
        Log.d("onGetAllRoomsDone" + roomIds, "onGetAllRoomsDone" + roomIds.length);
        if(roomIds!=null && roomIds.length>0){
            theClient.getLiveRoomInfo(roomIds[0]);
            roomIdCounter++;
        }
    }

    @Override
    public void onDisconnectDone(ConnectEvent connectEvent) {

    }
    @Override
    public void onInitUDPDone(byte b) {
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
    public void onSubscribeRoomDone(RoomEvent roomEvent) {

    }
    @Override
    public void onUnSubscribeRoomDone(RoomEvent roomEvent) {

    }
    @Override
    public void onLeaveRoomDone(RoomEvent roomEvent) {

    }
    @Override
    public void onSetCustomRoomDataDone(LiveRoomInfoEvent liveRoomInfoEvent) {

    }
    @Override
    public void onUpdatePropertyDone(LiveRoomInfoEvent liveRoomInfoEvent) {

    }
    @Override
    public void onLockPropertiesDone(byte b) {

    }
    @Override
    public void onUnlockPropertiesDone(byte b) {

    }
    @Override
    public void onDeleteRoomDone(RoomEvent roomEvent) {

    }
    @Override
    public void onCreateRoomDone(RoomEvent roomEvent) {

    }
    @Override
    public void onGetOnlineUsersDone(AllUsersEvent allUsersEvent) {

    }
    @Override
    public void onGetLiveUserInfoDone(LiveUserInfoEvent liveUserInfoEvent) {

    }
    @Override
    public void onSetCustomUserDataDone(LiveUserInfoEvent liveUserInfoEvent) {

    }
    @Override
    public void onGetMatchedRoomsDone(MatchedRoomsEvent matchedRoomsEvent) {

    }
    @Override
    public void onGetRoomsCountDone(RoomEvent roomEvent) {

    }
    @Override
    public void onGetUsersCountDone(AllUsersEvent allUsersEvent) {

    }
}
