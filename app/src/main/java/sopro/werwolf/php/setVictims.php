<?php

/*
 * Following code will set the current victims
 * All details are read from HTTP Post Request
 * GameID, victimWer/ victimDor is needed
 */

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

// check for required fields
if (isset($_GET['gameID']) && isset($_GET['victimWer'])) {

    $gameID = $_GET['gameID'];
    $victimWer = $_GET['victimWer'];

    // set victimWer
    mysql_query("UPDATE game SET victimWer='$victimWer' WHERE gameID = '$gameID'")
	or die("Die Änderung des Werwolfopfers ist fehlgeschlagen");

    // check if victimWer has been changed

    $result = mysql_query("SELECT victimWer FROM game WHERE gameID = '$gameID'");
    if ($result == $victimWer) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "VictimWer successfully updated.";

        // echoing JSON response
        echo json_encode($response);
    }

 else if (isset($_GET['gameID']) && isset($_GET['victimDor'])) {

     $gameID = $_GET['gameID'];
     $victimDor = $_GET['victimDor'];

     // set victimDor
     mysql_query("UPDATE game SET victimDor='$victimDor' WHERE gameID = '$gameID'")
 	or die("Die Änderung des Bauernopfers ist fehlgeschlagen");

     // check if victimDor has been changed

     $result = mysql_query("SELECT victimDor FROM game WHERE gameID = '$gameID'");
     if ($result == $victimDor) {
         // successfully updated
         $response["success"] = 1;
         $response["message"] = "VictimDor successfully updated.";

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