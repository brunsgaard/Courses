<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<style type="text/css">
h1 {
	text-align: center;
}

.centered {
	text-align: center;
}

thead {
	font-weight: bold;
	text-align: center;
}

thead tr td,tbody tr td {
	border-bottom: #000 1px dotted;
	border-right: #000 1px dotted;
}

tfoot tr td {
	border-right: #000 1px dotted;
}

thead tr td.last,tbody tr td.last,tfoot tr td.last {
	border-right: none;
}

tfoot {
	font-weight: bold;
}

div.item {
	clear: left;
	width: 100%;
	overflow: hidden;
}

div.item div {
	float: left;
}

div.item div.label {
	width: 160px;
	font-weight: bold;
}

div.orderdetails {
	text-align: center;
}

div.orderdetails table {
	margin: 20px auto;
}
</style>

</head>

<body>

	<h1>Show order</h1>
	<div class="centered">
		<form name="input" action="problem3_2.php" method="get">
			<input type="text" name="query"
				value="<?php echo isset($_REQUEST["query"]) ? $_REQUEST["query"] : ""; ?>" />
			<input type="submit" value="Search" />
		</form>
	</div>

	<div class="info">

		<?php

		// Creating the PDO object with the connection to the db
		$db = new PDO("pgsql:host=qwa.dk;port=5432;dbname=dbwa2","dbwa2","diku2012");
		$db->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);

		/*
		 * If valid user input are present, then prepare and execute transaction
		* 	The transaction are adding a new product to the order or updating an already existing product.
		* 	The quantity added to the order are subtracted from unitsinstock at the nw_peoducts table.
		*/
		if(isset($_REQUEST["quantity"]) && !empty($_REQUEST["quantity"]) && is_numeric($_REQUEST["quantity"]) &&
				isset($_REQUEST["discount"]) && is_numeric($_REQUEST["discount"])
				&& ($_REQUEST["discount"]) >= 0 && ($_REQUEST["discount"]) <= 1){


			$db->beginTransaction();

			$db->exec("LOCK TABLE nw_orderdetail IN SHARE MODE");

			// Check how many of the needed product are in stock.
			$stmt = $db->prepare("SELECT unitsinstock FROM nw_product WHERE productid=:productid;");
			$stmt->execute(array(":productid"=>$_REQUEST["productid"]));

			// Only if unitsinstock is greater than the quantity required.
			if($stmt->fetchColumn() >= $_REQUEST["quantity"]) {

				// Check if the product are already in the order
				$stmt = $db->prepare("SELECT orderid FROM nw_orderdetail WHERE orderid=:orderid and productid=:productid");
				$stmt->execute(array(":orderid"=>$_REQUEST["query"],":productid"=>$_REQUEST["productid"]));


				// If the product are alaready present, then update nw_orderdeatil by adding the reqursted quantity to the existing quantity.
				if($stmt->rowCount()>0){

					$stmt = $db->prepare("UPDATE nw_orderdetail SET quantity = quantity +:quantity WHERE orderid =:orderid AND productid=:productid");
					$stmt->execute(array(":orderid"=>$_REQUEST["query"],":quantity"=>$_REQUEST["quantity"],":productid"=>$_REQUEST["productid"]));

				// Else insert into new row in nw_orderdetail
				} else {

					$stmt = $db->prepare("INSERT INTO nw_orderdetail (orderid,productid,unitprice,quantity,discount)
							VALUES(:orderid,:productid,(SELECT unitprice FROM nw_product WHERE productid=:productid),:quantity,:discount)");

					$stmt->execute(array(":orderid"=>$_REQUEST["query"],":productid"=>$_REQUEST["productid"],
							":quantity"=>$_REQUEST["quantity"],":discount"=>$_REQUEST["discount"]));

				}
				
				// At last, remove the added quantity from the unitsinstock
				$stmt = $db->prepare("UPDATE nw_product SET unitsinstock= unitsinstock-:quantity WHERE productid =:productid");
				$stmt->execute(array(":quantity"=>$_REQUEST["quantity"],":productid"=>$_REQUEST["productid"]));
			}
			
			//end transation
			$db->commit();
		}
		
		/*
		 * If valid user input (orderid) are present prepare and executeg query
		* else print "You have to enter a valid orderid..."
		*/
		if (isset($_REQUEST["query"]) && !empty($_REQUEST["query"]) && (int) $_REQUEST["query"] != 0 && is_numeric($_REQUEST["query"]) && 2147483647 >= $_REQUEST["query"]) {

			$stmt = $db->prepare(" SELECT orderid,orderdate,requireddate,shippeddate,freight,contactname,firstname,lastname,shipname,shipaddress,shipcity,shippostalcode,shipcountry FROM (nw_employee NATURAL JOIN nw_order) JOIN nw_customer ON nw_order.customerid=nw_customer.customerid WHERE orderid=:query;");
			$stmt->execute(array(":query"=>$_REQUEST["query"]));

			$row = $stmt->fetch(PDO::FETCH_ASSOC);

			
			/*
			 * If valid user input are present and result from SQL execution are not empty, then proceed
			 * by printing general order infomation. Else print "Order not found...."
			*/
			
			if(!empty($row)) {

				?>
		<div class="item">
			<div class="label">Order ID</div>
			<div class="value">
				<?php echo $row['orderid']; ?>
			</div>
		</div>
		<div class="item">
			<div class="label">Order date</div>
			<div class="value">
				<?php echo $row['orderdate']; ?>
				<strong>Required date</strong>
				<?php echo $row['requireddate']; ?>
				<strong>Shipped date</strong>
				<?php echo $row['shippeddate']; ?>
			</div>
		</div>
		<div class="item">
			<div class="label">Freight</div>
			<div class="value">
				<?php echo $row['freight']; ?>
			</div>
		</div>
		<div class="item">
			<div class="label">Customer contact</div>
			<div class="value">
				<?php echo $row['contactname']; ?>
			</div>
		</div>
		<div class="item">
			<div class="label">Employee contact</div>
			<div class="value">
				<?php echo $row['firstname']." ".$row['lastname']; ?>
			</div>
		</div>
		<div class="item">
			<div class="label">Ship name</div>
			<div class="value">
				<?php echo $row['shipname']; ?>
				<strong>Address</strong>
				<?php echo $row['shipaddress']; ?>
				<strong>City</strong>
				<?php echo $row['shipcity']; ?>
				<strong>Postalcode</strong>
				<?php echo $row['shippostalcode']; ?>
				<strong>Country</strong>
				<?php echo $row['shipcountry']; ?>
			</div>
		</div>
	</div>
	<div class="orderdetails">
		<table border="0" cellspacing="0" cellpadding="10" width="80%">
			<thead>
				<tr>
					<td class="first">Productid</td>
					<td>Productname</td>
					<td>Unitprice</td>
					<td>Quantity</td>
					<td>Discount</td>
					<td class="last">Price</td>
				</tr>
			</thead>
			<tbody>

				<?php 
				/*
				 * Prepare and execute a SQL statement gathering orderdeatails 
				 */
				$stmt = $db->prepare("SELECT nw_orderdetail.productid,productname,nw_orderdetail.unitprice,quantity,discount,(nw_orderdetail.unitprice * quantity) AS price  FROM nw_orderdetail JOIN nw_product ON nw_orderdetail.productid=nw_product.productid WHERE orderid=:query ORDER BY productid");
				$stmt->execute(array(":query"=>$_REQUEST["query"]));
				
				// variable to hold for Total cost
				$sum = 0;
				
				/*
				 *  As long as it is possible to fetch rows from $stmt
				*  then take one at a time, add the price from each row to $sum and print the row as a HTML.
				*/
				
				while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){

					// For each row, add price to  the total cost.
					$sum += $row['price'];

					echo "<tr>";
					echo "<td class=\"first\">{$row['productid']}</td>";
					echo "<td>{$row['productname']}</td>";
					echo "<td>{$row['unitprice']}</td>";
					echo "<td>{$row['quantity']}</td>";
					echo "<td>{$row['discount']}</td>";
					echo "<td class=\"last\">{$row['price']}</td>";
					echo "</tr>\n";
				}
				?>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="5">Total</td>
					<td class="last"><?php $sum?>
					</td>
				</tr>

			</tfoot>
		</table>
	</div>
	<div class="centered">
		<form name="add" action="problem3_2.php" method="get">

			<div class="field">
				<label for="productid">Productnavn</label> <select name="productid"
					id="productid">
					<?php
					$stmt = $db->prepare("SELECT productid,productname FROM nw_product ORDER BY productname ASC");
					$stmt->execute();

					while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
						echo "<option value=\"{$row['productid']}\">{$row['productname']}</option>\n";
					}
					?>
				</select> <label for="quantity">Quantity</label> <input type="text"
					name="quantity" id="quantity"> <label for="discount">Discount</label>
				<input type="text" name="discount" id="discount" value="0.0">
			</div>
			<input type="hidden" name="query"
				value=<?php echo isset($_REQUEST["query"]) ? $_REQUEST["query"] : ""; ?> />
			<input type="submit" value="Add" />
		</form>
	</div>

	<?php
			} else {
				echo "Ohhh Nooo... The orderid you searched for was not found";
			}
		} else{
			echo "You have to enter a valid ordernr e.g. 10455!";
		}
		?>

</body>
</html>
