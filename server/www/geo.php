<?php

error_reporting(-1);
if (isset($_POST['lon']) && isset($_POST['lat']) && isset($_POST['usr']) && isset($_POST['activity'])) {
    $db = mysqli_connect('localhost', 'root', 'happyhacking', 'fistbump');
    $q = 'INSERT INTO `fistbump`.`geo` (`lon`, `lat`, `usr`, `activity`) VALUES (' . "'$_POST[lon]', '$_POST[lat]', '$_POST[usr]', '$_POST[activity]') ON DUPLICATE KEY UPDATE lon=VALUES(lon), lat=VALUES(lat), activity=VALUES(activity)";
    //echo $q . "<br />";
    if (!$db->query($q)) {
        echo $db->error;
    }
    return;
}
if (isset($_GET['lon']) && isset($_GET['lat'])) {
    $spread_m = 300;
    $earth_r = 6371137;
    $spread_lat = 180 / pi() * $spread_m / $earth_r;
    $spread_long = 180 / pi() * $spread_m / ($earth_r * cos(pi() * $_GET['lat'] / 180));

    $long_min = $_GET['lon'] - $spread_long;
    $long_max = $_GET['lon'] + $spread_long;
    $lat_min = $_GET['lat'] - $spread_lat;
    $lat_max = $_GET['lat'] + $spread_lat;

    $db = mysqli_connect('localhost', 'root', 'happyhacking', 'fistbump');
    $q = "SELECT * FROM geo WHERE lon BETWEEN $long_min AND $long_max AND lat BETWEEN $lat_min AND $lat_max";
    //echo $q;
    $result = $db->query($q);

    if (!$result) {
        echo $db->error;
    }
    $return = [];
    $delete = [];
    while ($row = mysqli_fetch_assoc($result)) {
        if (time() - strtotime($row['timestamp']) > 600) {
            $delete[] = $row;
            continue;
        }
        $return[] = $row;
    }
    foreach ($delete as $v) {
        $q ="DELETE FROM geo WHERE timestamp='" . $v['timestamp'].'\'';
        //echo $q;
        $db->query($q);
    }
    echo json_encode($return);
}
?>