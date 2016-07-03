package sopro.werwolf.DatabaseConnection;

import android.app.ProgressDialog;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sopro.werwolf.Database;
import sopro.werwolf.JSONParser;

/**
 * Created by Alex on 28.06.2016.
 */
public class databaseCon{
    private String role;
    private static String lover;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDiaglog;

    //constructor
    public databaseCon(){

    }

    private static final String url_initialize_database = "http://www-e.uni-magdeburg.de/jkloss/initialize_database.php";
    private static final String url_player_details = "http://www-e.uni-magdeburg.de/jkloss/get_all_player.php";
    private static final String url_get_game_details = "http://www-e.uni-magdeburg.de/jkloss/get_game_details.php";


    /*
     *   role - which role was chosen?
     *   action - which action was chosen?
     *      -> important for "Hexe" -> used "Trank" or want to know if there is a "Trank" left
     *       -> important for "Dieb" -> action = new Role
     *   SPlayerID - selected Player
     *   gameID - current game
     */
    public String Role(String role, String action, int SPlayerID, String gameID){

        Database db = new Database();
        // ToDO: aktuelle Datenbank laden => als Argument an Methoden übergeben?
        switch(role){
            case "Amor":
                setLover(lover, SPlayerID, gameID);
                break;

            case "Dieb":
                changeRole(action, SPlayerID, gameID);
                break;

            case "Werwolf":
                victimSelected("Werwolf", SPlayerID, gameID);
                break;

            case "Hexe":
                switch(action){
                    case "show":
                        String victim = showVictimWer(gameID);
                        return victim;

                    case "Heiltrank":
                        String usedHeilen = String.valueOf(Traenke("heilen", gameID)); // check if "Heiltrank" was used till now
                        return usedHeilen;

                    case "Gifttrank":
                        String usedGift = String.valueOf(Traenke("gift", gameID)); // check if "Gifttrank" was used till now
                        return usedGift;

                    case "TrankVerwendenGift":
                        TrankVerwenden("gift", SPlayerID, gameID); // "Hexe" wants to use "Gifttrank"
                        break;

                    case "TrankVerwendenHeilen":
                        TrankVerwenden("heilen", 0, gameID); // "Hexe" wants to use "Heiltrank"
                        break;
                }
                break;

            case "Seherin":
                String goodOrBad = showRole(SPlayerID, gameID);
                return goodOrBad;

            case "Dorfbewohner":
                victimSelected("Dorfbewohner", SPlayerID, gameID);
                break;
        }

        return null;
    }


    private void setLover(String lover, int SplayerID, String gameID)
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("gameID", gameID));


        JSONObject jsonObject = jsonParser.makeHttpRequest(url_get_game_details, "GET", params);

        try {
            // check which lover is selected and if this lover is already selected
            if(lover == "lover1" && (jsonObject.getJSONObject("game").getInt("lover1") == 0))
            {
                jsonObject.put("lover1", SplayerID);
            }

            // just in case lover1 is already selected - maybe not necessary
            else if (lover == "lover1" && (jsonObject.getJSONObject("game").getInt("lover1") != 0))
            {
                pDiaglog.setMessage("Fehler: setLover - Lover1 wurde bereits ausgewählt");
            }

            else if(lover == "lover2" && (jsonObject.getJSONObject("game").getInt("lover2") == 0))
            {
                jsonObject.put("lover2", SplayerID);
            }

            // analogous to lover1
            else pDiaglog.setMessage("Fehler: setLover - Lover2 wurde bereits ausgewählt");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // ToDo: update databse

    }

    private String showVictimWer(String gameID)
    {
        String victimWer = null;
        List<NameValuePair> IDparams = new ArrayList<NameValuePair>();
        IDparams.add(new BasicNameValuePair("gameID", gameID));

        JSONObject victimID = jsonParser.makeHttpRequest(url_get_game_details, "GET", IDparams);

        List<NameValuePair> NameParams = new ArrayList<NameValuePair>();
        NameParams.add(new BasicNameValuePair("gameID", gameID));

        JSONObject jsonObject = jsonParser.makeHttpRequest(url_player_details, "GET", NameParams);

        int victimWerID = 0;
            try {
                victimWerID = victimID.getJSONObject("game").getInt("victimWer"); // get ID of the victim

                JSONArray array = jsonObject.getJSONArray("player");

                for(int i = 0; i < array.length(); i++)
                {
                    if(array.getJSONObject(i).getInt("id") == victimWerID)
                    {
                        victimWer = array.getJSONObject(i).getString("name"); // get the name of the ID
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        return victimWer;
    }

    private boolean Traenke(String trank, String gameID){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("gameID", gameID));

        JSONObject jsonObject = jsonParser.makeHttpRequest(url_get_game_details, "GET", params);

        if(trank == "gift") {
            try {
                int gift = jsonObject.getJSONObject("game").getInt("victimHex");
                if (gift == 0) {
                    return true; // "Gifttrank" wasn't used jet
                }

                else return false;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else if(trank == "heilen") {
            try {
                int heilen = jsonObject.getJSONObject("game").getInt("heiltrank");
                if (heilen == 1) {
                    return true;    // "Heiltrank" wasn't used jet
                } else return false;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private void TrankVerwenden(String trank, int SplayerID, String gameID)
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("gameID", gameID));

        JSONObject jsonObject = jsonParser.makeHttpRequest(url_get_game_details, "GET", params);

        if(trank == "heilen")
        {
            try {
                jsonObject.put("heiltrank", 0); // "Heiltrank" was used
                jsonObject.put("victimWer", null); // no victim of the werewolves this night

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else
        {
            try {
                jsonObject.put("victimHex", SplayerID); // "Gifttrank" was used -> tag victim

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //ToDo: update Database
    }


    private String showRole(int SPlayerID, String gameID)
    {
        String goodOrBad = null;
        String playerID = String.valueOf(SPlayerID);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("playerID", playerID));
        params.add(new BasicNameValuePair("gameID", gameID));


        JSONObject jsonObject = jsonParser.makeHttpRequest(url_player_details, "GET", params);

        try {
            String SRole = jsonObject.getJSONObject("player").getString("role");

            if(SRole == "werwolf" || SRole == "Werwolf")
            {
                goodOrBad = "böse";
            }

            else goodOrBad = "gut";

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return goodOrBad;
    }

    private void changeRole(String newRole, int SPlayerID, String gameID)
    {
        String playerID = String.valueOf(SPlayerID);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("gameID", gameID));
        params.add(new BasicNameValuePair("playerID", playerID));

        JSONObject jsonObject = jsonParser.makeHttpRequest(url_player_details, "GET", params);

        try {
            jsonObject.put("role", newRole);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //ToDo: update database
    }

    private void victimSelected(String role, int SPlayerID, String gameID)
    {
        String playerID = String.valueOf(SPlayerID);
        List<NameValuePair> paramsPlayer = new ArrayList<NameValuePair>();
        paramsPlayer.add(new BasicNameValuePair("gameID", gameID));
        paramsPlayer.add(new BasicNameValuePair("playerID", playerID));

        List<NameValuePair> paramsGame = new ArrayList<NameValuePair>();
        paramsGame.add(new BasicNameValuePair("gameID", gameID));

        JSONObject victim = jsonParser.makeHttpRequest(url_player_details, "GET", paramsPlayer); // victim must be tagged as dead
        JSONObject murderer = jsonParser.makeHttpRequest(url_get_game_details, "GET", paramsGame); // current victim must be saved

        try {
            victim.put("alive", 0);

            if(role == "Werwolf")
            {
                murderer.put("victimWer", SPlayerID);
            }

            else murderer.put("victimDor", SPlayerID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //ToDo: update database
    }



    public List<Integer> checkDeadPlayers(String gameID)
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        List<Integer> deadPlayer = new ArrayList<>();
        params.add(new BasicNameValuePair("gameID", gameID));

        JSONObject jsonObject = jsonParser.makeHttpRequest(url_player_details, "GET", params);

        try {
            JSONArray array = jsonObject.getJSONArray("player");

            for(int i = 0; i < array.length(); i++)
            {
                if(array.getJSONObject(i).getInt("alive") == 0)
                {
                    int deadNr = 0;
                    deadPlayer.add(deadNr, i);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return deadPlayer;
    }

}
