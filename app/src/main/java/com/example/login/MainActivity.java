package com.example.login;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    EditText user,pass;
    String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=(EditText)findViewById(R.id.loginUser);
        pass=(EditText)findViewById(R.id.loginPass);


    }

    public void login(View view){
        String urlstr;
        username=user.getText().toString();
        password=pass.getText().toString();
        //~
        //urlstr="http://192.168.0.102:5000/login";
        //urlstr="https://aqueous-forest-8384.herokuapp.com/login.php?user="+username+"&password="+password;
        urlstr="http://dilpreet-app.herokuapp.com/login";
        //urlstr="https://aqueous-forest-8384.herokuapp.com/login.php";
        URL url;
        try{
            url=new URL(urlstr);
            new GetData().execute(url);

        }
        catch (MalformedURLException e){

        }
    }

    public class GetData extends AsyncTask<URL,Void,String>{


        @Override
        protected String doInBackground(URL... params) {

            HttpURLConnection httpURLConnection=null;
            InputStream inputStream=null;
            StringBuilder builder=new StringBuilder();
            int success=1;

            try {
                httpURLConnection=(HttpURLConnection) params[0].openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                //httpURLConnection.setRequestProperty("Content-Type", "application/json");

                httpURLConnection.connect();

                String query = String.format("username=%s&password=%s",
                        URLEncoder.encode(username, "UTF-8"),
                        URLEncoder.encode(password, "UTF-8"));

                ;


                OutputStream writer=new BufferedOutputStream(httpURLConnection.getOutputStream());
                writer.write(query.getBytes());
                writer.flush();
                writer.close();


                success=httpURLConnection.getResponseCode();

                if(success==HttpURLConnection.HTTP_OK){
                    inputStream=httpURLConnection.getInputStream();
                    InputStreamReader reader=new InputStreamReader(inputStream);
                    BufferedReader bufferedReader=new BufferedReader(reader);
                     builder=new StringBuilder();
                    String line;

                    while((line=bufferedReader.readLine())!=null){
                        builder.append(line);
                    }

                }

            }
            catch (IOException e){
                success=0;

                Log.d("My", "Error Connecting: "+e);
            }

            finally {
                httpURLConnection.disconnect();
            }

          return builder.toString();
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();
        }
    }


}
