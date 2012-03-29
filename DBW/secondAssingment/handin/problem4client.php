<?php

/*
 * This is the test client for problem 4 (web services).
 *
 * The code in this class are very clean,
 * so it is my opinion that code comments are not requred here.
 */

header('Content-type: text/plain; charset=utf-8');
$client = new SoapClient('problem4.wsdl',array('features'=>SOAP_SINGLE_ELEMENT_ARRAYS,'location'=>'http://qwa.dk/problem4server.php'));

// Client testing for problem 4.1
$request = new stdClass();
$request->customerid = 'FURIB';
$result = $client->findOrders($request);
echo "For customerid = FURIB findOrders\n\n";
echo ">>";print_r($result);

// Client testing for problem 4.2
$request = new stdClass();
$request->productid = 43;
$result = $client->findSoldProduct($request);
echo "For productid = 43 findSoldProduct\n\n";
echo ">>"; print_r($result);

// Client testing for problem 4.3
$request = new stdClass();
$request->orderid = 10455;
$result = $client->getOrder($request);
echo "For orderid = 10455 getOrder\n\n";
echo ">>"; print_r($result);

// Client testing for problem 4.4
$request = new stdClass();
$request->orderid = 10455;
$result = $client->findOrderDetails($request);
echo "For orderid = 10455 findOrderDetails\n\n";
echo ">>"; print_r($result);

// Client testing for problem 4.5
$request = new stdClass();
$request->year = 1996;
$result = $client->getProductStatistics($request);
echo "For year = 1996 getProductStatistics\n\n";
echo ">>"; print_r($result);
?>
