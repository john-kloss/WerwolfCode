package sopro.werwolf.DatabaseConnection;

import android.os.StrictMode;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sopro.werwolf.JSONParser;

/**
 * Created by Alex on 28.06.2016.
 */
public class databaseCon{
    JSONParser jsonParser = new JSONParser();

    //constructor
    public databaseCon(){

    }

    private static final String url_get_all_player = "http://192.168.0.13/SoPro/db_alt/get_all_player.php";//"http://www-e.uni-magdeburg.de/jkloss/get_all_player.php";
    private static final String url_get_game_details = "http://192.168.0.13/SoPro/db_alt/get_game_details.php"; //"http://www-e.uni-magdeburg.de/jkloss/get_game_details.php";
    private static final String url_get_player_details = "http://192.168.0.13/SoPro/db_alt/get_player_details.php"; //"http://www-e.uni-magdeburg.de/jkloss/get_player_details.php";
    private static final String url_change_Role = "http://192.168.0.13/SoPro/db_alt/changeRole.php"; //"http://www-e.uni-magdeburg.de/jkloss/changeRole.php";
    private static final String url_update_Hexe = "http://192.168.0.13/SoPro/db_alt/updateHexe.php"; //"http://www-e.uni-magdeburg.de/jkloss/updateHexe.php";
    private static final String url_set_Victims = "http://192.168.0.13/SoPro/db_alt/setVictims.php"; //"http://www-e.uni-magdeburg.de/jkloss/setVictims.php";
    private static final String url_change_Alive = "http://192.168.0.13/SoPro/db_alt/changeAlive.php"; //"http://www-e.uni-magdeburg.de/jkloss/changeAlive.php";
    private static final String url_set_Lovers = "http://192.168.0.13/SoPro/db_alt/setLovers.php"; //"http://www-e.uni-magdeburg.de/jkloss/setLovers.php";


    /*
     *   role - which role was chosen?
     *   action - which action was chosen?
     *      -> important for "Hexe" -> used "Trank" or want to know if there is a "Trank" left
     *      -> important for "Dieb" -> action = new Role
     *   SPlayerID - selected Player
     *   gameID - current game
     */

    public String Role(String role, String action, int PlayerID, String gameID){

        String SPlayerID = String.valueOf(PlayerID);

        switch(role){
            case "Amor":
                setLover(action, SPlayerID, gameID);
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
                        return usedHeilen; // true = wasn't used; false = was used

                    case "Gifttrank":
                        String usedGift = String.valueOf(Traenke("gift", gameID)); // check if "Gifttrank" was used till now
                        return usedGift; // true = wasn't used; false = was used

                    case "TrankVerwendenGift":
                        TrankVerwenden("gift", SPlayerID, gameID); // "Hexe" wants to use "Gifttrank"
                        break;

                    case "TrankVerwendenHeilen":
                        TrankVerwenden("heilen", SPlayerID, gameID); // "Hexe" wants to use "Heiltrank"
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


    private void setLover(String lover, String SplayerID, String gameID)
    {
        // ich weiß nicht genau, was es macht aber es sorgt dafür, dass "GET" funktioniert
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("gameID", gameID));
        // check which lover is selected and if this lover is already selected
        if (lover == "lover1")
        {
            params.add(new BasicNameValuePair("lover1", SplayerID));
        }

        else
        {
            params.add(new BasicNameValuePair("lover2", SplayerID));
        }

        //ToDo: update database
        //ToDo: check for success
        jsonParser.makeHttpRequest(url_set_Lovers, "POST", params);
    }

    private String showVictimWer(String gameID)
    {
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String victimWer = null;
        List<NameValuePair> IDparams = new ArrayList<NameValuePair>();
        IDparams.add(new BasicNameValuePair("gameID", gameID));

        JSONObject victimID = jsonParser.makeHttpRequest(url_get_game_details, "GET", IDparams);

        List<NameValuePair> NameParams = new ArrayList<NameValuePair>();
        NameParams.add(new BasicNameValuePair("gameID", gameID));

        JSONObject jsonObject = jsonParser.makeHttpRequest(url_get_all_player, "GET", NameParams);

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
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("gameID", gameID));

        JSONObject jsonObject = jsonParser.makeHttpRequest(url_get_game_details, "GET", params);

        try{
            JSONArray JTrank = jsonObject.getJSONArray("game");
            if(trank == "gift") {

                try{
                JTrank.getJSONObject(JTrank.length()-1).getInt("victimHex");
                } catch (JSONException e) {
                    return true;
                }
            }

            else if(trank == "heilen") {
                int heilen = JTrank.getJSONObject(JTrank.length()-1).getInt("heiltrank");

                if (heilen == 1) {
                    return true;    // "Heiltrank" wasn't used jet
                }

                else return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void TrankVerwenden(String trank, String SplayerID, String gameID)
    {
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("gameID", gameID));

        if(trank == "heilen")
        {
                params.add(new BasicNameValuePair("heiltrank", "0")); // "Heiltrank" was used
                params.add(new BasicNameValuePair("victimWer", null));// no victim of the werewolves this night
        }

        else
        {
                params.add(new BasicNameValuePair("victimHex", SplayerID));// "Gifttrank" was used -> tag victim
        }

        //ToDo: update Database
        //ToDo: check for success
        jsonParser.makeHttpRequest(url_update_Hexe, "POST", params);
    }


    private String showRole(String SPlayerID, String gameID)
    {
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String goodOrBad = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("playerID", SPlayerID));
        params.add(new BasicNameValuePair("gameID", gameID));

        JSONObject jsonObject = jsonParser.makeHttpRequest(url_get_player_details, "GET", params);

        try {
            JSONArray JRole = jsonObject.getJSONArray("player");
            String SRole = JRole.getJSONObject(JRole.length()-1).getString("role");

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

    private void changeRole(String newRole, String SPlayerID, String gameID)
    {
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("gameID", gameID));
        params.add(new BasicNameValuePair("playerID", SPlayerID));
        params.add(new BasicNameValuePair("newRole", newRole));

        //ToDo: update database
        //ToDO: check for success
        jsonParser.makeHttpRequest(url_change_Role, "POST", params);
    }

    private void victimSelected(String role, String SPlayerID, String gameID)
    {
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        List<NameValuePair> paramsPlayer = new ArrayList<NameValuePair>();
        paramsPlayer.add(new BasicNameValuePair("gameID", gameID));
        paramsPlayer.add(new BasicNameValuePair("playerID", SPlayerID));

        List<NameValuePair> paramsGame = new ArrayList<NameValuePair>();
        paramsGame.add(new BasicNameValuePair("gameID", gameID));

        paramsPlayer.add(new BasicNameValuePair("alive", "0")); // victim tagged as dead

        // current victim must be saved
        if(role == "Werwolf")
        {
            paramsGame.add(new BasicNameValuePair("victimWer", SPlayerID));
        }

        else paramsGame.add(new BasicNameValuePair("victimDor", SPlayerID));

        //ToDo: update database
        //ToDo: check for success

        jsonParser.makeHttpRequest(url_change_Alive, "POST", paramsPlayer);
        jsonParser.makeHttpRequest(url_set_Victims, "POST", paramsGame);
    }

    public List<Integer> checkDeadPlayers(String gameID)
    {
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        List<Integer> deadPlayer = new ArrayList<>();
        params.add(new BasicNameValuePair("gameID", gameID));

        JSONObject jsonObject = jsonParser.makeHttpRequest(url_get_all_player, "GET", params);

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

    public void changeAlive(String gameID, String phase)
    {
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String victimWerID = null;
        String victimDorID = null;
        String victimHexID = null;

        List<NameValuePair> IDparams = new ArrayList<NameValuePair>();
        IDparams.add(new BasicNameValuePair("gameID", gameID));

        JSONObject victimID = jsonParser.makeHttpRequest(url_get_game_details, "GET", IDparams);

        List<NameValuePair> WerParams = new ArrayList<NameValuePair>();
        WerParams.add(new BasicNameValuePair("gameID", gameID));

        List<NameValuePair> DorParams = new ArrayList<NameValuePair>();
        DorParams.add(new BasicNameValuePair("gameID", gameID));

        List<NameValuePair> HexParams = new ArrayList<NameValuePair>();
        HexParams.add(new BasicNameValuePair("gameID", gameID));

        try {
            if(phase == "nacht") {
                victimWerID = String.valueOf(victimID.getJSONObject("game").getInt("victimWer")); // get ID of the victim
                victimHexID = String.valueOf(victimID.getJSONObject("game").getInt("victimHex")); // get ID of the victim
                WerParams.add(new BasicNameValuePair("playerID", victimWerID));
                HexParams.add(new BasicNameValuePair("playerID", victimHexID));

                //ToDo: update database
                //ToDo: check for success
                jsonParser.makeHttpRequest(url_change_Alive, "POST", WerParams);
                jsonParser.makeHttpRequest(url_change_Alive, "POST", HexParams);
            }

            else {
                victimDorID = String.valueOf(victimID.getJSONObject("game").getInt("victimDor")); // get ID of the victim
                DorParams.add(new BasicNameValuePair("playerID", victimDorID));

                //ToDo: update database
                //ToDo: check for success
                jsonParser.makeHttpRequest(url_change_Alive, "POST", DorParams);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}