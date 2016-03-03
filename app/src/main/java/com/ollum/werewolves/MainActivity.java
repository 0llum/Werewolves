package com.ollum.werewolves;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;

public class MainActivity extends AppCompatActivity implements ConnectionRequestListener{

    private WarpClient theClient;
    private ProgressDialog progressDialog;

    UserLocalStore userLocalStore;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();

        String userName = user.username;

        Utils.USER_NAME  = userName;
        progressDialog = ProgressDialog.show(this, "", "Please wait...");
        progressDialog.setCancelable(true);
        theClient.addConnectionRequestListener(this);
        theClient.connectWithUserName(userName);
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
        Log.d("OnConnectDone", ""+event.getResult());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                    progressDialog=null;
                }
                if(event.getResult()==WarpResponseResultCode.SUCCESS){
                    Toast.makeText(MainActivity.this, "Connection Success", Toast.LENGTH_SHORT).show();
                    joinChatroom();
                }else{
                    Toast.makeText(MainActivity.this, "Connection Failed"+event.getResult(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void joinChatroom() {
        startActivity(new Intent(this, ResultActivity.class));
    }

    @Override
    public void onDisconnectDone(ConnectEvent event) {

    }
    @Override
    public void onInitUDPDone(byte arg0) {


    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(theClient!=null){
            theClient.removeConnectionRequestListener(this);
        }
    }
}
