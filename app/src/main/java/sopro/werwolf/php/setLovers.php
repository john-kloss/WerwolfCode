<?php

/*
 * Following code will set the Lovers for the current game
 * All details are read from HTTP Post Request
 * GameID, Lover1 and Lover2 is needed
 */

$gameID = 1;

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

// check for required fields
if (isset($_GET['gameID']) && isset($_GET['lover1'])) {

    $gameID = $_GET['gameID'];
    $lover1 = $_GET['lover1'];

    // set lover1
    mysql_query("UPDATE game SET lover1='$lover1' WHERE gameID = '$gameID'")
	    or die("Setzen des ersten Lovers ist fehlgeschlagen");


     // check if lover1 has been set
    $result = mysql_query("SELECT lover1 FROM game WHERE gameID = '$gameID'");
    if ($result == $lover1) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Player successfully added.";

        // echoing JSON response
        echo json_encode($response);
    }

else if (isset($_GET['gameID']) && isset($_GET['lover2'])) {

        $gameID = $_GET['gameID'];
        $lover2 = $_GET['lover2'];

        // set lover2
        mysql_query("UPDATE game SET lover2='$lover2' WHERE gameID = '$gameID'")
    	    or die("Setzen des zweiten Lovers ist fehlgeschlagen");


         // check if lover2 has been set
        $result = mysql_query("SELECT lover2 FROM game WHERE gameID = '$gameID'");
        if ($result == $lover2) {
            // successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "Player successfully added.";

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