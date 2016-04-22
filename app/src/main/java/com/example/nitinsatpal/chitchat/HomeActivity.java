package com.example.nitinsatpal.chitchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class HomeActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {
    EditText mUserText;
    Button mSendButton;
    TextView mResponse;
    public static final String REQUEST_TAG = "MainVolleyActivity";
    /*private static final String apiKey = "6nt5d1nJHkqbkphe";
    private static final String externalID = "chirag1";
    private static String message = " ";
    private static final int chatBotID = 63906;*/
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mUserText = (EditText) findViewById(R.id.user_text);
        mSendButton = (Button) findViewById(R.id.send_button);
        mResponse = (TextView) findViewById(R.id.response);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Cache cache = new DiskBasedCache(this.getCacheDir(), 10 * 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        mQueue = new RequestQueue(cache, network);
        mQueue.start();

        /*JSONObject object = new JSONObject();
        try {
            object.put("apiKey", apiKey);
            object.put("chatBotID", chatBotID);
            object.put("externalID", externalID);
            object.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUserText.getText().toString().equals(""))
                    Toast.makeText(HomeActivity.this, "please enter a message", Toast.LENGTH_SHORT).show();
                else {
                    String encodedParam = "";
                    try{
                        encodedParam = URLEncoder.encode(mUserText.getText().toString(), "UTF-8");
                    } catch (Exception e){
                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    final String url = "http://www.personalityforge.com/api/chat/?apiKey=6nt5d1nJHkqbkphe&message="+encodedParam+"&chatBotID=63906&externalID=chirag1";
                    final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url,
                            new JSONObject(), HomeActivity.this, HomeActivity.this);
                    jsonRequest.setTag(REQUEST_TAG);
                    mQueue.add(jsonRequest);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mResponse.setText(error.getMessage());
    }

    @Override
    public void onResponse (Object response) {
        try {
            if(((JSONObject) response).getInt("success") == 1)
                mResponse.setText(((JSONObject) response).getJSONObject("message").getString("message")) ;
            else
                mResponse.setText(((JSONObject) response).getString("errorMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}