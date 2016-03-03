package com.ollum.werewolves;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyData;
import com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;

public class ChatActivity extends AppCompatActivity implements RoomRequestListener, NotifyListener{

    private WarpClient theClient;
    private TextView outputView;
    private EditText inputEditText;
    private ScrollView outputScrollView;
    private Button sendtBtn;
    private Spinner onlineUsers;
    private String roomId;
    private ArrayList<String> onlineUserList = new ArrayList<String>();

    UserLocalStore userLocalStore;
    User user;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        outputView = (TextView)findViewById(R.id.outputTextView);
        inputEditText = (EditText)findViewById(R.id.inputEditText);
        sendtBtn = (Button)findViewById(R.id.sendBtn);
        outputScrollView = (ScrollView)findViewById(R.id.outputScrollView);
        onlineUsers = (Spinner)findViewById(R.id.onlineUserSpinner);
        roomId = "";
        roomId = getIntent().getStringExtra("roomId");
        try{
            theClient = WarpClient.getInstance();
        }catch(Exception e){
            e.printStackTrace();
        }
        theClient.addRoomRequestListener(this);
        theClient.subscribeRoom(roomId);
        theClient.addNotificationListener(this);
        theClient.getLiveRoomInfo(roomId);

        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();

        setUserAFK(user);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setUserAFK(user);
        theClient.sendChat(user.username + " has come online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        setUserOffline(user);
        theClient.sendChat(user.username + " has gone offline");
    }

    public void onDestroy(){
        super.onDestroy();
        if(theClient!=null){
            theClient.removeRoomRequestListener(this);
            theClient.unsubscribeRoom(roomId);
            theClient.removeNotificationListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(roomId.length()>0){
            theClient.leaveRoom(roomId);
            theClient.disconnect();
        }
    }

    public void onSendClicked(View view){
        outputScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        theClient.sendChat(user.username + ": " +inputEditText.getText().toString());
        inputEditText.setText("");
    }

    @Override
    public void onGetLiveRoomInfoDone(final LiveRoomInfoEvent event) {
        if(event.getResult()==0){
            if(event.getJoinedUsers().length>1){// if more than one user is online
                final String onlineUser[] = Utils.removeUsernameFromArray(event.getJoinedUsers());
                for(int i=0;i<onlineUser.length;i++){
                    onlineUserList.add(onlineUser[i].toString());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fillDataInSpinner(event.getData().getName());
                    }
                });
            }else{ // Alert for no online user found
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.showToast(ChatActivity.this, "No online user found");
                    }
                });
                Log.d("No online user found", "No online user found");
            }
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.showToast(ChatActivity.this, "Error in fetching data. Please try later");
                }
            });
        }
    }

    private void fillDataInSpinner(String name){
        if(name!=null && name.length()>0){
            onlineUsers.setPrompt(name);// room name
        }
        String onlineUserArray[] = new String[onlineUserList.size()];
        for(int i=0;i<onlineUserArray.length;i++){
            onlineUserArray[i] = onlineUserList.get(i).toString();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, onlineUserArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        onlineUsers.setAdapter(adapter);
    }

    @Override
    public void onJoinRoomDone(RoomEvent arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onLeaveRoomDone(RoomEvent arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onSetCustomRoomDataDone(LiveRoomInfoEvent arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onSubscribeRoomDone(RoomEvent arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onUnSubscribeRoomDone(RoomEvent arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onUpdatePropertyDone(LiveRoomInfoEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChatReceived(final ChatEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                outputView.append("\n"+event.getMessage());
            }
        });
    }

    @Override
    public void onPrivateChatReceived(final String userName, final String message) {

    }
    @Override
    public void onRoomCreated(RoomData arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onRoomDestroyed(RoomData arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onUpdatePeersReceived(UpdateEvent arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onUserJoinedLobby(LobbyData arg0, String arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUserJoinedRoom(final RoomData roomData, final String userName) {
        theClient.sendChat(userName + " entered the room");
        if(userName.equals(Utils.USER_NAME)==false){
            onlineUserList.add(userName);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fillDataInSpinner(null);
                }
            });
        }
    }

    @Override
    public void onUserLeftLobby(LobbyData arg0, String arg1) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onUserLeftRoom(final RoomData roomData, final String userName) {
        theClient.sendChat(userName + " left the room");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onlineUserList.remove(userName);
                fillDataInSpinner(null);
            }
        });
    }

    @Override
    public void onMoveCompleted(MoveEvent arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onLockPropertiesDone(byte arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onUnlockPropertiesDone(byte arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onUserPaused(String arg0, boolean arg1, String arg2) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onUserResumed(String arg0, boolean arg1, String arg2) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onGameStarted(String arg0, String arg1, String arg2) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onGameStopped(String arg0, String arg1) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onUserChangeRoomProperty(RoomData arg0, String arg1, HashMap<String, Object> arg2, HashMap<String, String> arg3) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onNextTurnRequest(String arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onPrivateUpdateReceived(String arg0, byte[] arg1, boolean arg2) {
        // TODO Auto-generated method stub

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
