package com.ollum.werewolves;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;

public class ResultActivity extends AppCompatActivity implements ZoneRequestListener, RoomRequestListener{

    private WarpClient theClient;
    HashMap<String, Object> propertiesToMatch;
    private String roomIdJoined = "";
    //	private boolean isOnJoinRoom = false;
    private String[] roomIds;
    private int roomIdCounter = 0;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("on create", "Result Activity");
        setContentView(R.layout.activity_result);

        try {
            theClient = WarpClient.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(propertiesToMatch==null){
            propertiesToMatch = new HashMap<String, Object>();
        }else{
            propertiesToMatch.clear();
        }
        propertiesToMatch.put("topic", "chatroom");
        roomIdCounter = 0;
        roomIds = null;
        theClient.joinRoomWithProperties(propertiesToMatch);
        roomIdJoined = "";
    }

    @Override
    public void onJoinRoomDone(final RoomEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(event.getResult()==WarpResponseResultCode.SUCCESS){
                    roomIdJoined = event.getData().getId();
                    joinChatroom();
                }
            }
        });
    }

    public void joinChatroom() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("roomId", roomIdJoined);
        startActivity(intent);    }

    public void onStart(){
        super.onStart();
        theClient.addZoneRequestListener(this);
        theClient.addRoomRequestListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(roomIdJoined.length()>0){
            theClient.leaveRoom(roomIdJoined);
            theClient.disconnect();
        }
    }

    public void onStop(){
        super.onStop();
        theClient.removeZoneRequestListener(this);
        theClient.removeRoomRequestListener(this);
    }

    @Override
    public void onGetLiveRoomInfoDone(final LiveRoomInfoEvent event) {
        HashMap roomProperties = event.getProperties();
        Log.d("roomProperties"+roomProperties, "propertiesToMatch"+propertiesToMatch);

        theClient.joinRoom("" + event.getData().getId());
    }


    @Override
    public void onLeaveRoomDone(RoomEvent event ) {

    }
    @Override
    public void onSetCustomRoomDataDone(LiveRoomInfoEvent arg0) {

    }
    @Override
    public void onSubscribeRoomDone(RoomEvent arg0) {

    }
    @Override
    public void onUnSubscribeRoomDone(RoomEvent arg0) {

    }
    @Override
    public void onUpdatePropertyDone(LiveRoomInfoEvent arg0) {

    }
    @Override
    public void onCreateRoomDone(RoomEvent arg0) {

    }
    @Override
    public void onDeleteRoomDone(RoomEvent arg0) {

    }
    @Override
    public void onGetAllRoomsDone(AllRoomsEvent event) {
        roomIds = event.getRoomIds();
        Log.d("onGetAllRoomsDone"+roomIds, "onGetAllRoomsDone"+roomIds.length);
        if(roomIds!=null && roomIds.length>0){
            theClient.getLiveRoomInfo(roomIds[0]);
            roomIdCounter++;
        }
    }
    @Override
    public void onGetLiveUserInfoDone(LiveUserInfoEvent event) {

    }
    @Override
    public void onGetMatchedRoomsDone(MatchedRoomsEvent arg0) {

    }
    @Override
    public void onGetOnlineUsersDone(AllUsersEvent arg0) {

    }
    @Override
    public void onSetCustomUserDataDone(LiveUserInfoEvent arg0) {

    }
    @Override
    public void onLockPropertiesDone(byte arg0) {

    }
    @Override
    public void onUnlockPropertiesDone(byte arg0) {

    }

    /* (non-Javadoc)
     * @see com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener#onGetRoomsCountDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent)
     */
    @Override
    public void onGetRoomsCountDone(RoomEvent arg0) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener#onGetUsersCountDone(com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent)
     */
    @Override
    public void onGetUsersCountDone(AllUsersEvent arg0) {
        // TODO Auto-generated method stub

    }
}
