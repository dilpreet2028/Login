package com.example.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class Signup extends AppCompatActivity {

    String username,password;
    EditText user,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        user=(EditText)findViewById(R.id.signupUser);
        pass=(EditText)findViewById(R.id.signupPass);
    }

    public void create(View view){
        String urlstr;
        username=user.getText().toString();
        password=pass.getText().toString();

        urlstr="http://dilpreet-app.herokuapp.com/signup";
        //urlstr="http://localhost:5000/signup";

        URL url;
        try{
            url=new URL(urlstr);
            new GetData().execute(url);

        }
        catch (MalformedURLException e){

        }
    }

    public class GetData extends AsyncTask<URL,Void,Integer> {


        @Override
        protected Integer doInBackground(URL... params) {

            HttpURLConnection httpURLConnection=null;
            InputStream inputStream=null;
            StringBuilder builder=new StringBuilder();
            int success=1,result=0;

            try {
                httpURLConnection=(HttpURLConnection) params[0].openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                String query = String.format("username=%s&password=%s",
                        URLEncoder.encode(username, "UTF-8"),
                        URLEncoder.encode(password, "UTF-8"));



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

               JSONObject json=new JSONObject(builder.toString());
                result=json.getInt("success");

            }
            catch (Exception e){
                success=0;
            }


            finally {
                httpURLConnection.disconnect();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(result==1){
                Toast.makeText(getApplicationContext(), "Signed up", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }

            else
                Toast.makeText(getApplicationContext(),"try again",Toast.LENGTH_LONG).show();
        }
    }


}
