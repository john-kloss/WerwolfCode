<?php
 
/*
 * Following code will get single player details
 * A player is identified by his/her id and the gameID
 */
 
// array for JSON response
$response = array();

// connecting to db
$link = mysql_connect("localhost", "jkloss", "werwolf")
or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
or die("Auswahl der Datenbank fehlgeschlagen");


// check for post data
if (isset($_GET["playerID"]) && isset($_GET["gameID"])) {

    $playerID = $_GET['playerID'];
    $gameID = $_GET['gameID'];
 
    // get a player from player table
    $result = mysql_query("SELECT * FROM player WHERE playerID = '$playerID' AND gameID = '$gameID'");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $player = array();
            $player["playerID"] = $result["playerID"];
            $player["gameID"] = $result["gameID"];
            $player["name"] = $result["name"];
            $player["role"] = $result["role"];
            $player["alive"] = $result["alive"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["player"] = array();
 
            array_push($response["player"], $player);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no player found
            $response["success"] = 0;
            $response["message"] = "No player found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no player found
        $response["success"] = 0;
        $response["message"] = "No player found";
 
        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>