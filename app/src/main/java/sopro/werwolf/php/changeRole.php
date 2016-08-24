<?php

/*
 * Following code will change the Role of the former "Dieb"
 * All details are read from HTTP Post Request
 * GameID, PlayerID and newRole is needed
 */

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

// check for required fields
if (isset($_GET['gameID']) && isset($_GET['playerID']) && isset($_GET['newRole']) {

    $gameID = $_GET['gameID'];
    $playerID = $_GET['gameID'];
    $newRole = $_GET['newRole'];

    // set new Role
    mysql_query("UPDATE player SET role='$newRole' WHERE playerID = '$playerID' AND gameID = '$gameID'")
	or die("Die Änderung der Rolle ist fehlgeschlagen");

    // check if role has been changed

    $result = mysql_query("SELECT role FROM player WHERE playerID = '$playerID' AND gameID = '$gameID'");
    if ($result == $newRole) {
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