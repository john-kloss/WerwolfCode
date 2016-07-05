package sopro.werwolf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JoinGameActivity extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDiaglog;
    String name;
    String gameID;
    private static final String url_create_player = "http://www-e.uni-magdeburg.de/jkloss/create_player.php";
    private static final String TAG_SUCCESS = "success";


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

        new createPlayer().execute();
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

}
