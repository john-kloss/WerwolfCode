
<?php
 
/*
 * Following code will create a new player row
 * All details are read from HTTP Post Request
 * GameID is needed
 */

$gameID = 1;
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['gameID']) && isset($_POST['name'])) {
 
    $gameID = $_POST['gameID']
    $name = $_POST['name'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // select playerID
    $return = mysql_query("SELECT playerID FROM player WHERE name IS NULL AND gameID = "$gameID" LIMIT 1")
	or die("Auswahl der playerID ist fehlgeschlagen");
    $playerID = mysql_fetch_array($return, MYSQL_NUM);
    // set player name
    $result = mysql_query("UPDATE player SET name='Legolas' WHERE playerID = "$playerID"") //replace with $name
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
