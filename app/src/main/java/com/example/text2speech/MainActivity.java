package com.example.text2speech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.media.MediaPlayer;
import android.widget.Spinner;
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
    // Declaring all the variables
    EditText text;
    Button convert;
    MediaPlayer mediaPlayer;
    private RequestQueue requestQueue;
    Spinner dropDown;
    private String Text = "";
    private String Speaker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);
        convert = findViewById(R.id.convertBTN);
        dropDown = findViewById(R.id.dropDown);
        requestQueue = Volley.newRequestQueue(this);

        String[] speaker = new String[]{"Select Speaker", "Joanna", "Joey"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, speaker);
        dropDown.setAdapter(adapter);

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Text = text.getText().toString();
                if(Speaker.equals("") || Text.equals("") || Speaker.equals("Select Speaker")){
                    Toast.makeText(MainActivity.this, "Please enter text and select a speaker", Toast.LENGTH_SHORT).show();
                }
                else {
                    CallAPI(Text, Speaker);
                }
            }
        });

        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Speaker = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Calling the API
    void CallAPI(String Text, String speaker){
        // Please paste the API URL here
        String apiUrl = "secret";

        // Creating a JSON String to pass into the API.
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("text", Text); // Adding the text we want to convert to speech
            jsonBody.put("speaker", speaker); // Adding the Voice ID
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Creating a request object with JSON String as Input
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String statusCode = response.getString("statusCode");
                    if(statusCode.equals("500")){
                        Toast.makeText(MainActivity.this, "Error while using API", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // If StatusCode = 200, we will extract the temporary URL from the response body.
                        String presignedUrl = response.getString("body");
                        // Using the temporary url to play the speech
                        playAudio(presignedUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.toString());
                }
            }
        );
        // Actually calling the API from here.
        requestQueue.add(request);
    }

    private void playAudio(String presignedUrl) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        // Creating MediaPlayer object and assigning it to play the .mp3 file from the temporary URL.
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
