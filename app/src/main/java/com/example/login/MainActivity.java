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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    String urlstr;
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

        username=user.getText().toString();
        password=pass.getText().toString();
        //~
        //urlstr="http://192.168.0.102:5000/login";
        //urlstr="https://aqueous-forest-8384.herokuapp.com/login.php?user="+username+"&password="+password;
        urlstr="http://dilpreet-app.herokuapp.com/login";
        //urlstr="http://localhost:5000/login";
        //urlstr="https://aqueous-forest-8384.herokuapp.com/login.php";
       /* URL url;
        try{
            url=new URL(urlstr);
            new GetData().execute(url);

        }
        catch (MalformedURLException e){

        }*/
        requestToServer();
    }

    public void signup(View v){
        startActivity(new Intent(getApplicationContext(),Signup.class));
    }

    public void requestToServer(){
        RequestQueue requestQueue= Volley.newRequestQueue(this);

        StringRequest myreq=new StringRequest(Request.Method.POST, urlstr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String checkpass=parseJSON(response);
                        if(checkpass!=null){
                            int result=verifyPass(checkpass,password);
                            if(result==1)
                                Toast.makeText(MainActivity.this,"Logged in",Toast.LENGTH_LONG).show();

                        }
                        else{
                            Toast.makeText(MainActivity.this,"Try Again...",Toast.LENGTH_LONG).show();
                        }
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"error :"+error,Toast.LENGTH_LONG).show();
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<String,String>();
                params.put("username",username);
                params.put("password",password);
                return params;
            }
        };

        requestQueue.add(myreq);

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
