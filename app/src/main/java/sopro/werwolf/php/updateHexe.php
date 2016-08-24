<?php

/*
 * Following code will change settings for the "Hexe" (victimHex, heiltrank)
 * All details are read from HTTP Post Request
 * GameID, victimHex/ heiltrank is needed
 */

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

// check for required fields
if (isset($_GET['gameID']) && isset($_GET['victimHex'])) {

    $gameID = $_GET['gameID'];
    $victimHex = $_GET['victimHex'];

    // set victimHex
    mysql_query("UPDATE game SET victimHex='$victimHex' WHERE gameID = '$gameID'")
	or die("Die Änderung des Hexenopfers ist fehlgeschlagen");

    // check if victimHex has been changed

    $result = mysql_query("SELECT victimHex FROM game WHERE gameID = '$gameID'");
    if ($result == $victimHex) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "VictimHex successfully updated.";

        // echoing JSON response
        echo json_encode($response);
    }

 else if (isset($_GET['gameID']) && isset($_GET['heiltrank'])) {

        $gameID = $_GET['gameID'];
        $heiltrank = $_GET['heiltrank'];

        // set heiltrank
        mysql_query("UPDATE game SET heiltrank='$heiltrank' WHERE gameID = '$gameID'")
    	or die("Die Änderung des Heiltranks ist fehlgeschlagen");

        // check if heiltrank has been changed

        $result = mysql_query("SELECT heiltrank FROM game WHERE gameID = '$gameID'");
        if ($result == $heiltrank) {
            // successfully updated
            $response["success"] = 1;
            $response["message"] = "heiltrank successfully updated.";

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