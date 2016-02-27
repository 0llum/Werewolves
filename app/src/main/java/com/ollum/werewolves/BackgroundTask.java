package com.ollum.werewolves;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTask extends AsyncTask<String, Void, String>{

    Context ctx;

    //UserLocalStore userLocalStore = new UserLocalStore(ctx);

    public BackgroundTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
     protected String doInBackground(String... params) {
        String reg_url = "http://0llum.bplaced.net/Werewolves/SignUp.php";
        String login_url = "http://0llum.bplaced.net/Werewolves/Login.php";
        String addFriend_url = "http://0llum.bplaced.net/Werewolves/AddFriend.php";
        String method = params[0];

        if (method.equals("signUp")) {
            String username = params[1];
            String password = params[2];
            String email = params[3];

            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =   URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                                URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                                URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return response.trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (method.equals("login")) {
            String username = params[1];
            String password = params[2];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =   URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                                URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return response.trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (method.equals("addFriend")) {
            String username_1 = params[1];
            String username_2 = params[2];

            try {
                URL url = new URL(addFriend_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =   URLEncoder.encode("username_1", "UTF-8") + "=" + URLEncoder.encode(username_1, "UTF-8") + "&" +
                        URLEncoder.encode("username_2", "UTF-8") + "=" + URLEncoder.encode(username_2, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return response.trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }

    @Override
    protected void onPostExecute(String result) {
        switch (result) {
            case ("Signing up successful"):
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                ctx.startActivity(new Intent(ctx, Login.class));
                break;
            case ("Signing up failed"):
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                break;
            case ("Username already exists"):
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                break;
            case ("Email address already exists"):
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                break;
            case ("Login successful"):
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                ctx.startActivity(new Intent(ctx, Menu.class));
                //userLocalStore.setUserLoggedIn(true);
                break;
            case ("Login failed, incorrect user data"):
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                break;
            case ("Friend added"):
                ctx.startActivity(new Intent(ctx, Friendlist.class));
                //BackgroundTaskRecyclerView backgroundTaskRecyclerView = new BackgroundTaskRecyclerView(ctx);
                //backgroundTaskRecyclerView.execute();
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                break;
            case ("User could not be found"):
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                break;
            case ("User is already your friend"):
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                break;
            case ("Friend could not be added"):
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                break;
        }
        //Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
    }
}
