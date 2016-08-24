<?php

/*
 * Following code will change "alive"
 * All details are read from HTTP Post Request
 * GameID, PlayerID is needed
 */

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

// check for required fields
if (isset($_GET['gameID']) && isset($_GET['playerID'])) {

    $gameID = $_GET['gameID'];
    $playerID = $_GET['gameID'];

    // set new Role
    mysql_query("UPDATE player SET alive=0 WHERE playerID = '$playerID' AND gameID = '$gameID'")
	or die("Die Änderung von "alive" ist fehlgeschlagen");

    // check if alive has been changed

    $result = mysql_query("SELECT alive FROM player WHERE playerID = '$playerID' AND gameID = '$gameID'");
    if ($result == 0) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Player successfully updated.";

        // echoing JSON response
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