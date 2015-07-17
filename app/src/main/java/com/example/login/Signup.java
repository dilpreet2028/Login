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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.HashMap;
import java.util.Map;


public class Signup extends AppCompatActivity {

    String username,password;
    EditText user,pass;
    String urlstr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        user=(EditText)findViewById(R.id.signupUser);
        pass=(EditText)findViewById(R.id.signupPass);
    }

    public void create(View view){

        username=user.getText().toString();
        password=pass.getText().toString();

        urlstr="http://dilpreet-app.herokuapp.com/signup";
        //urlstr="http://localhost:5000/signup";
        requestToServer();

    }

    public void requestToServer(){
        RequestQueue queue= Volley.newRequestQueue(Signup.this);
        StringRequest request=new StringRequest(Request.Method.POST, urlstr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        checkResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<String,String>();
                params.put("username",username);
                params.put("password",password);
                return params;
            }
        };
        queue.add(request);

    }

    private void checkResponse(String response){
        try{
            JSONObject json=new JSONObject(response);
            int result=json.getInt("success");
            if(result==1)
                Toast.makeText(Signup.this,"Succesfully Signed Up",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this,"Unable to Signup",Toast.LENGTH_SHORT).show();
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }


}
