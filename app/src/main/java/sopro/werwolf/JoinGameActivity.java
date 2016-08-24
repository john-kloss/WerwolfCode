package sopro.werwolf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JoinGameActivity extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    JSONArray players = null;
    ArrayList<HashMap<String, String>> playerList;
    ProgressDialog pDiaglog;
    String name;
    String gameID;
    private static final String url_create_player = "http://www-e.uni-magdeburg.de/jkloss/create_player.php";
    private static final String url_get_all_player = "http://www-e.uni-magdeburg.de/jkloss/get_all_player.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PLAYERS = "players";
    private static final String TAG_PLAYERID = "playerID";
    private static final String TAG_GAMEID = "gameID";
    private static final String TAG_NAME = "name";
    private static final String TAG_ROLE = "role";
    private static final String TAG_ALIVE = "alive";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void joinGame(View view){
        EditText editText = (EditText) findViewById(R.id.gameID);
        gameID = editText.getText().toString();

        //inserting player's name into table
        new createPlayer().execute();

        // Hashmap containing the players
        playerList = new ArrayList<HashMap<String, String>>();

        //loading all players in background
        //new getAllPlayer().execute();


        String[] cards = null;
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("cards", cards);
        startActivity(intent);
    }

    /*
     * This class inserts a name into 'player'.
     * @param gameID
     */
    class createPlayer extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDiaglog = new ProgressDialog(JoinGameActivity.this);
            pDiaglog.setMessage("Erstelle Spieler...");
            pDiaglog.setIndeterminate(false);
            pDiaglog.setCancelable(true);
            pDiaglog.show();
        }
        protected String doInBackground(String... args) {
            String name = "John";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("gameID", gameID));

            JSONObject json = jsonParser.makeHttpRequest(url_create_player, "POST", params);

            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");

                if (success == 1) {
                    // successfully created player
                    Snackbar.make(findViewById(R.id.joinGameView), "Spieler erstellt", Snackbar.LENGTH_SHORT).show();
                } else {
                    // failed to create player
                    Snackbar.make(findViewById(R.id.joinGameView), "Spieler nicht erstellt", Snackbar.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
            pDiaglog = new ProgressDialog(JoinGameActivity.this);
            pDiaglog.setMessage("Lade Spielerdetails...");
            pDiaglog.setIndeterminate(false);
            pDiaglog.setCancelable(true);
            pDiaglog.show();
        }
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_get_all_player, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Player: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // players found
                    // Getting Array of Players
                    players = json.getJSONArray(TAG_PLAYERS);

                    // looping through all players
                    for (int i = 0; i < players.length(); i++) {
                        JSONObject c = players.getJSONObject(i);

                        // Storing each json item in variable
                        String playerID = c.getString(TAG_PLAYERID);
                        String gameID = c.getString(TAG_GAMEID);
                        String name = c.getString(TAG_NAME);
                        String role = c.getString(TAG_ROLE);
                        String alive = c.getString(TAG_ALIVE);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PLAYERID, playerID);
                        map.put(TAG_GAMEID, gameID);
                        map.put(TAG_NAME, name);
                        map.put(TAG_ROLE, role);
                        map.put(TAG_ALIVE, alive);

                        // adding HashList to ArrayList
                        playerList.add(map);
                    }
                } else {
                    // no players found
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
        public void onPostExecute(String file_url) {
            pDiaglog.dismiss();
        }
    }

}
