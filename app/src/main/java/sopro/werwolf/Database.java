package sopro.werwolf;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Database extends AppCompatActivity {

    private ProgressDialog pDiaglog;
    String id;
    EditText txtName;
    EditText txtRole;

    JSONParser jsonParser = new JSONParser();


    private static final String url_initialize_table = "http://www-e.uni-magdeburg.de/jkloss/initialize_table.php";
    private static final String url_create_player = "http://www-e.uni-magdeburg.de/jkloss/create_player.php";
    private static final String url_get_player_details = "http://www-e.uni-magdeburg.de/jkloss/get_player_details.php";
    private static final String url_get_all_player = "http://www-e.uni-magdeburg.de/jkloss/get_all_player.php";
    private static final String url_get_game_details = "http://www-e.uni-magdeburg.de/jkloss/get_game_details.php";

    private static final String url_update_player = "http://www-e.uni-magdeburg.de/jkloss/update_player.php";
    private static final String url_update_game = "http://www-e.uni-magdeburg.de/jkloss/update_game.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PLAYER = "player";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ROLE = "role";
    private static final String TAG_LOVER = "lover";
    private static final String TAG_ALIVE = "alive";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new initializeDatabase().execute();


    }

    /*
     * This class initializes the database.
     * It creates a new column in 'game' with a new gameID.
     * Afterwards, according to the numOfPlayers, new rows with role and playerID
     * will be inserted into 'player'.
     * @param numOfPlayer&roles[]
     */
    class initializeDatabase extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDiaglog = new ProgressDialog(Database.this);
            pDiaglog.setMessage("Initialisiere Datenbank. Bitte warten...");
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
                    //try {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();

                    //HTTP request with get
                    JSONObject jsonObject = jsonParser.makeHttpRequest(url_initialize_table, "GET", params);
                    /*}
                    catch (JSONException e) {
                    e.printStackTrace();
                    }*/

                }
            });
            return null;
        }

        public void onPostExecute(String file_url) {
            pDiaglog.dismiss();
        }
    }

    /*
     * This class inserts a name into 'player'.
     * @param gameID
     */
    class createPlayer extends AsyncTask<String, String, String>{
        protected void onPreExecute() {
            super.onPreExecute();
            pDiaglog = new ProgressDialog(Database.this);
            pDiaglog.setMessage("Stelle Verbindung mit dem Spiel her...");
            pDiaglog.setIndeterminate(false);
            pDiaglog.setCancelable(true);
            pDiaglog.show();
        }
        protected String doInBackground(String... params) {
            //update UI from Background thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            return null;
        }
        public void onPostExecute(String file_url) {
            pDiaglog.dismiss();
        }
    }

    /*
     * This class gets the details for a specific player.
     * @param gameID&playerID
     */
    class getPlayerDetails extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDiaglog = new ProgressDialog(Database.this);
            pDiaglog.setMessage("Lade Spielerdetails...");
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
                        JSONObject jsonObject = jsonParser.makeHttpRequest(url_get_player_details, "POST", params);


                        Log.d("Alle Spieler: ", jsonObject.toString());

                        //check for success
                        success = jsonObject.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            //received player details
                            JSONArray playerObj = jsonObject.getJSONArray(TAG_PLAYER);
                            //get the first player object from JSON Array
                            JSONObject player = playerObj.getJSONObject(0);

                            txtName = (EditText) findViewById(R.id.txtName);
                            txtRole = (EditText) findViewById(R.id.txtRole);
                            txtName.setText(player.getString(TAG_NAME));
                            txtRole.setText(player.getString(TAG_ROLE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }

        public void onPostExecute(String file_url) {
            pDiaglog.dismiss();
        }
    }

    /*
     * Gets all player details
     * @param gameID
     */
    class getAllPlayer extends AsyncTask<String, String, String>{
        protected void onPreExecute() {
            super.onPreExecute();
            pDiaglog = new ProgressDialog(Database.this);
            pDiaglog.setMessage("Lade Spielerdetails...");
            pDiaglog.setIndeterminate(false);
            pDiaglog.setCancelable(true);
            pDiaglog.show();
        }
        protected String doInBackground(String... params) {
            //update UI from Background thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            return null;
        }
        public void onPostExecute(String file_url) {
            pDiaglog.dismiss();
        }
    }

    /*
     * Gets game details
     * @param gameID
     */
     class getGameDetails extends AsyncTask<String, String, String>{
        protected void onPreExecute() {
            super.onPreExecute();
            pDiaglog = new ProgressDialog(Database.this);
            pDiaglog.setMessage("Lade Spielinformationen...");
            pDiaglog.setIndeterminate(false);
            pDiaglog.setCancelable(true);
            pDiaglog.show();
        }
        protected String doInBackground(String... params) {
            //update UI from Background thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            return null;
        }
        public void onPostExecute(String file_url) {
            pDiaglog.dismiss();
        }
    }

    /*
     * Updates player details
     * @param gameID&playerID(&name)(&role)(&alive)
     */
    class updatePlayer extends AsyncTask<String, String, String>{
        protected void onPreExecute() {
            super.onPreExecute();
            pDiaglog = new ProgressDialog(Database.this);
            pDiaglog.setMessage("Update Spielerinformationen...");
            pDiaglog.setIndeterminate(false);
            pDiaglog.setCancelable(true);
            pDiaglog.show();
        }
        protected String doInBackground(String... params) {
            //update UI from Background thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            return null;
        }
        public void onPostExecute(String file_url) {
            pDiaglog.dismiss();
        }
    }

    /*
     * Updates game details
     * @param gameID(&lover1)(&lover2)(&victimHex)(&heiltrank)(&victimWer)(&victimDor)
     */
     class updateGame extends AsyncTask<String, String, String>{
        protected void onPreExecute() {
            super.onPreExecute();
            pDiaglog = new ProgressDialog(Database.this);
            pDiaglog.setMessage("Update Spielinformationen...");
            pDiaglog.setIndeterminate(false);
            pDiaglog.setCancelable(true);
            pDiaglog.show();
        }
        protected String doInBackground(String... params) {
            //update UI from Background thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            return null;
        }
        public void onPostExecute(String file_url) {
            pDiaglog.dismiss();
        }
    }



}


