<?php

header('Content-type: text/plain; charset=utf-8');

$db = new PDO("pgsql:host=qwa.dk;port=5432;dbname=dbwa2","dbwa2","diku2012");
$db->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);

$stmt = $db->prepare("SELECT orderprint(orderid) FROM nw_order WHERE orderid=:orderid");
$stmt->execute(array(":orderid"=>11071));

print_r($stmt->fetchColumn());

?>