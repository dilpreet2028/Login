package com.example.login;

import android.content.ContentValues;
import android.content.Intent;
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
        //urlstr="http://localhost:5000/login";
        //urlstr="https://aqueous-forest-8384.herokuapp.com/login.php";
        URL url;
        try{
            url=new URL(urlstr);
            new GetData().execute(url);

        }
        catch (MalformedURLException e){

        }
    }

    public void signup(View v){
        startActivity(new Intent(getApplicationContext(),Signup.class));
    }
    public class GetData extends AsyncTask<URL,Void,Integer>{


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
                //httpURLConnection.setRequestProperty("Content-Type", "application/json");

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

                String checkpass=parseJSON(builder.toString());
                Log.d("My",checkpass+"   "+builder.toString()+ "   "+password);

                if(checkpass!=null){
                    result=verifyPass(password,checkpass);
                    Log.d("My",result+"");
                }

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
            if(result==1)
                Toast.makeText(getApplicationContext(),"Logged in",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),"try again",Toast.LENGTH_LONG).show();
        }
    }


    ////Converting the json object to the required format.
    private String  parseJSON(String json){
        String key=null;
        try{
            JSONObject jsonObject=new JSONObject(json);
            int status=jsonObject.getInt("success");

            if(status==1){
                 key=jsonObject.getString("password");
            }

        }
        catch(JSONException e){

        }
        return key;
    }

    //Verifying the password
    private Integer verifyPass(String passone,String passtwo){
        if(passone.equals(passtwo))
            return 1;

        return 0;

    }

}
