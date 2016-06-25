create_player.php
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
if (isset($_POST['name'])) {
 
    $name = $_POST['name'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // select playerID
    $playerID("SELECT playerID FROM player WHERE name IS NULL AND gameID = "$gameID" LIMIT 1")
	or die("Auswahl der playerID ist fehlgeschlagen");
    // set player name
    $result = mysql_query("UPDATE player SET name='Legolas' WHERE playerID = "$playerID"")
	or die("Einfuegen des Namens ist fehlgeschlagen");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";
 
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
