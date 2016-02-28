package com.ollum.werewolves;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity implements View.OnClickListener{

    EditText etUsername, etPassword;
    Button bLogin, bSignUp;
    UserLocalStore userLocalStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etUsername = (EditText)findViewById(R.id.login_username);
        etPassword = (EditText)findViewById(R.id.login_password);

        bLogin = (Button)findViewById(R.id.login_login);
        bSignUp = (Button)findViewById(R.id.login_signup);

        bLogin.setOnClickListener(this);
        bSignUp.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login:
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString();
                User user = new User(username, password);
                logUserIn(user);
                setUserOnline(user);

                userLocalStore.storeUserData(user);

                break;

            case R.id.login_signup:
                finish();
                startActivity(new Intent(this, SignUp.class));
                overridePendingTransition(0, 0);

                break;
        }
    }

    private void logUserIn(User user) {
        String method = "login";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, user.username, user.password);
    }

    private void setUserOnline(User user) {
        String method = "online";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, user.username);
    }
}
