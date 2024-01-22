package com.example.text2speech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText text;
    Button convert;
    MediaPlayer mediaPlayer;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);
        convert = findViewById(R.id.convertBTN);
        requestQueue = Volley.newRequestQueue(this);

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Text = text.getText().toString();
                CallAPI(Text);
            }
        });
    }

    void CallAPI(String Text){
        String apiUrl = "secret";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("text", Text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String statusCode = response.getString("statusCode");
                    if(statusCode.equals("500")){
                        Toast.makeText(MainActivity.this, "Error while using API", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String presignedUrl = response.getString("body");
                        System.out.println(presignedUrl);
                        playAudio(presignedUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle API errors here
                }
            }
        );
        requestQueue.add(request);
    }

    private void playAudio(String presignedUrl) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(presignedUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}