package com.dipak.ownerv1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class MainActivity extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // products JSONArray
    JSONArray mess = null;

    JSONParser jParser = new JSONParser();
    private static String url_all_products = "https://wanidipak56.000webhostapp.com/receiveall.php";
    ArrayList<HashMap<String, String>> messList;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSINFO = "messinfo";
    private static final String TAG_MESSID = "messid";
    private static final String TAG_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialCalendarView mcv = (MaterialCalendarView) findViewById(R.id.calendarView);
        CalendarDay cd = mcv.getSelectedDate();
        CalendarDay mindate = mcv.getMinimumDate();
        CalendarDay maxdate = mcv.getMaximumDate();
        messList = new ArrayList<>();

        new LoadAllMess().execute();

        Button b = (Button) findViewById(R.id.button4);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewMessInsert.class);
                startActivity(intent);
            }
        });

        Button b1 = (Button) findViewById(R.id.button3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuInsert.class);
                startActivity(intent);
            }
        });

    }

    class LoadAllMess extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    mess = json.getJSONArray(TAG_MESSINFO);

                    // looping through All Products
                    for (int i = 0; i < mess.length(); i++) {
                        JSONObject c = mess.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_MESSID);
                        String name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_MESSID, id);
                        map.put(TAG_NAME, name);

                        Log.d("Dipak: Mess Id ", id);
                        Log.d("Dipak: Mess Name ", name);

                        // adding HashList to ArrayList
                        messList.add(map);
                    }
                } else {
                    // no products found
                    Log.d("Dipak: ", "No Mess found!");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    /*ListAdapter adapter = new SimpleAdapter(
                            AllProductsActivity.this, messList,
                            R.layout.list_item, new String[] { TAG_MESSID,
                            TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    // updating listview
                    setListAdapter(adapter);*/
                }
            });

        }

    }

}


