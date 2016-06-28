
<?php
 
/*
 * Following code will create a new player row
 * All details are read from HTTP Post Request
 * GameID is needed
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
if (isset($_GET['gameID']) && isset($_GET['name'])) {

    $gameID = $_GET['gameID'];
    $name = $_GET['name'];
    
    // select playerID
    $return = mysql_query("SELECT playerID FROM player WHERE name IS NULL AND gameID = '$gameID' LIMIT 1")
	    or die("Auswahl der playerID ist fehlgeschlagen");
    $playerID = mysql_fetch_array($return, MYSQL_NUM);

    echo  $playerID[0];


    // set player name
    $result = mysql_query("UPDATE player SET name='$name' WHERE playerID = '$playerID[0]' AND gameID = '$gameID'")
	    or die("Einfuegen des Namens ist fehlgeschlagen");

    // check if row inserted or not
    if ($result) {
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
