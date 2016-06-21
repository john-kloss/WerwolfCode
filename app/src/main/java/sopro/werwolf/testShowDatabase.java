package sopro.werwolf;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class testShowDatabase extends AppCompatActivity {

    private ProgressDialog pDiaglog;
    String id;
    EditText txtName;
    EditText txtRole;

    JSONParser jsonParser = new JSONParser();

    private static final String url_player_details = "http://www-e.uni-magdeburg.de/jkloss/create_table.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PLAYER = "player";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ROLE= "role";
    private static final String TAG_TEAM= "team";
    private static final String TAG_LOVER = "lover";
    private static final String TAG_ALIVE = "alive";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_show_database);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new getPlayerDetails().execute();



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }

    class getPlayerDetails extends AsyncTask<String, String, String>{

        protected void onPreExecute(){
            super.onPreExecute();
            pDiaglog = new ProgressDialog(testShowDatabase.this);
            pDiaglog.setMessage("Lade Spielerdetails. Bitte warten...");
            pDiaglog.setIndeterminate(false);
            pDiaglog.setCancelable(true);
            pDiaglog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //update UI from Background thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //check if it was successful
                    int success;
                    try {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();

                        //HTTP request with get
                        JSONObject jsonObject =jsonParser.makeHttpRequest(url_player_details, "GET", params);

                        Log.d("Alle Spieler: ", jsonObject.toString());

                        //check for success
                        success = jsonObject.getInt(TAG_SUCCESS);
                        if (success == 1){
                            //received player details
                            JSONArray playerObj = jsonObject.getJSONArray(TAG_PLAYER);
                            //get the first player object from JSON Array
                            JSONObject player = playerObj.getJSONObject(0);

                            txtName = (EditText) findViewById(R.id.txtName);
                            txtRole = (EditText) findViewById(R.id.txtRole);
                            txtName.setText(player.getString(TAG_NAME));
                            txtRole.setText(player.getString(TAG_ROLE));
                        }
                    }

                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }

        public void onPostExecute(String file_url){
            pDiaglog.dismiss();
        }
    }
}
