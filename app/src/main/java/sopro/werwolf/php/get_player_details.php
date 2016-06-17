get_player_details.php
<?php
 
/*
 * Following code will get single player details
 * A player is identified by his/her id
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// check for post data
if (isset($_GET["id"])) {
    $id = $_GET['id'];
 
    // get a product from player table
    $result = mysql_query("SELECT *FROM player WHERE id = $id");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $player = array();
            $player["id"] = $result["id"];
            $player["name"] = $result["name"];
            $player["role"] = $result["role"];
            $player["team"] = $result["team"];
            $player["lover"] = $result["lover"];
            $player["alive"] = $result["alive"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["player"] = array();
 
            array_push($response["product"], $player);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No player found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no product found
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
