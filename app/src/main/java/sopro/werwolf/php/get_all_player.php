<?php

/*
 * Following code will list all the players
 */

// array for JSON response
$response = array();

// connect to db
$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");

if (isset($_GET["gameID"])) {

    $gameID = $_GET["gameID"];
    // get all player from player table
    $result = mysql_query("SELECT * FROM player WHERE gameID = '$gameID'") or die(mysql_error());

    // check for empty result
    if (mysql_num_rows($result) > 0) {
        // looping through all results
        $response["players"] = array();

        while ($row = mysql_fetch_array($result)) {
            // temp user array
            $player = array();
            $player["playerID"] = $row["playerID"];
            $player["gameID"] = $row["gameID"];
            $player["name"] = $row["name"];
            $player["role"] = $row["role"];
            $player["alive"] = $row["alive"];

            // push single player into final response array
            array_push($response["players"], $player);
        }
        // success
        $response["success"] = 1;

        // echoing JSON response
        echo json_encode($response);
    } else {
        // no player found
        $response["success"] = 0;
        $response["message"] = "No players found";

        // echo no users JSON
        echo json_encode($response);
    }
}
else{
        // required field is missing
        $response["success"] = 0;
        $response["message"] = "Required field(s) is missing";

        // echoing JSON response
        echo json_encode($response);
    }

?>
