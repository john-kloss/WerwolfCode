<?php

/*
 * Following code will list all the players
 */

// array for JSON response
$response = array();

// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// get all products from products table
$result = mysql_query("SELECT *FROM products") or die(mysql_error());

// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    $response["players"] = array();

    while ($row = mysql_fetch_array($result)) {
        // temp user array
       	$player = array();
        $player["id"] = $row["id"];
        $player["name"] = $row["name"];
        $player["role"] = $row["role"];
        $player["team"] = $row["team"];
        $player["lover"] = $row["lover"];
        $player["alive"] = $row["alive"];

        // push single player into final response array
        array_push($response["players"], $player);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No players found";

    // echo no users JSON
    echo json_encode($response);
}
?>
