package com.dipak.ownerv1;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;


public class NewMessInsert extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mess_insert);

        Button btnCreateMess = (Button) findViewById(R.id.button);

        btnCreateMess.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream os = null;
                        InputStream is = null;
                        HttpURLConnection conn = null;
                        try {
                            //constants
                            URL url = new URL("https://wanidipak56.000webhostapp.com/posttry.php");
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("messid", "Mess4");
                            jsonObject.put("name", "Kwality");
                            jsonObject.put("guestcharge", "80");

                            String message = jsonObject.toString();

                            conn = (HttpURLConnection) url.openConnection();
                            conn.setReadTimeout( 10000 /*milliseconds*/ );
                            conn.setConnectTimeout( 15000 /* milliseconds */ );
                            conn.setRequestMethod("POST");
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            conn.setFixedLengthStreamingMode(message.getBytes().length);

                            //make some HTTP header nicety
                            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                            //open
                            conn.connect();

                            //setup send
                            os = new BufferedOutputStream(conn.getOutputStream());
                            os.write(message.getBytes());

                            //clean up
                            os.flush();

                            //do somehting with response
                            is = conn.getInputStream();
                            Log.d("Dipak :",is.toString());

                            //String contentAsString = readIt(is,len);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        } finally {
                            try
                            {
                                os.close();
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            conn.disconnect();
                        }
                    }
                }).start();
            }
        });
    }
}
