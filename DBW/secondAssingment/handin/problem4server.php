<?php

/*
 * This is the server solution for problem 4 (web services).
 * 
 * The code in this class are very clean, 
 * so it is my opinion that code comments are not requred here.
 */

$server = new SoapServer('problem4.wsdl', array('features'=>SOAP_SINGLE_ELEMENT_ARRAYS));
$server->setClass('NorthwindService');
$server->handle();

class NorthwindService {
	
	private $db;
	
	public function __construct(){
		$this->db = new PDO("pgsql:host=qwa.dk;port=5432;dbname=dbwa2","dbwa2","diku2012");
	}
	
	public function findOrders($params){
		
		$stmt = $this->db->prepare("SELECT orderid FROM nw_order WHERE customerid ILIKE :customerid");
		$stmt->execute(array(":customerid"=>$params->customerid));
		
		$return = new stdClass();
		$return->orderids = $stmt->fetchAll(PDO::FETCH_COLUMN);
		return $return;
	}
	
	public function findSoldProduct($params){
	
		$stmt = $this->db->prepare("SELECT sum(quantity) FROM nw_orderdetail WHERE productid=:productid");
		$stmt->execute(array(":productid"=>$params->productid));
	
		return $stmt->fetchColumn();
	}
	
	public function getOrder($params){
	
		$stmt = $this->db->prepare("SELECT customerid,employeeid,orderdate,requireddate,shippeddate,freight FROM nw_order WHERE orderid=:orderid");
		$stmt->execute(array(":orderid"=>$params->orderid));
		
		return $stmt->fetchObject();
	}
	
	public function findOrderDetails($params){
	
		$stmt = $this->db->prepare("SELECT productid,unitprice,quantity,discount  FROM nw_orderdetail WHERE orderid=:orderid");
		$stmt->execute(array(":orderid"=>$params->orderid));

		$return = new stdClass();
		$return->orderDetails = $stmt->fetchAll(PDO::FETCH_OBJ);
		return $return;
	}
	
	public function getProductStatistics($params){
	
		$stmt = $this->db->prepare("SELECT nw_product.productname,nw_customer.companyname,SUM(nw_orderdetail.quantity) AS totalquantity	FROM	nw_order	JOIN nw_orderdetail ON nw_order.orderid = nw_orderdetail.orderid	JOIN nw_product ON nw_orderdetail.productid = nw_product.productid	JOIN nw_customer ON nw_order.customerid = nw_customer.customerid	WHERE EXTRACT(YEAR FROM nw_order.orderdate) = :year	GROUP BY nw_product.productname, nw_customer.companyname	ORDER BY nw_product.productname, SUM(nw_orderdetail.quantity) DESC");
				$stmt->execute(array(":year"=>$params->year));
	
		$return = new stdClass();
		$return->out = $stmt->fetchAll(PDO::FETCH_OBJ);
		return $return;
	}
}
?>
