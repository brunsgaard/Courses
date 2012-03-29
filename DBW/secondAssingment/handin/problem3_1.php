<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<style type="text/css">
h1 {
	text-align: center;
}

thead {
	font-weight: bold;
	text-align: center;
}

.centered {
	text-align: center;
}

body {
	margin: 20px auto;
	width: 1100px;
}

div.field {
	clear: left;
	overflow: hidden;
	width: 60%;
	margin: 3px auto;
}

div.field label {
	width: 30%;
	float: left;
	text-align: left;
}

div.field input,div.field select {
	width: 70%;
	float: left;
}
</style>
</head>
<body>

	<h1>Add New Product</h1>
	<div class="centered">
		<form name="input" action="problem3_1.php" method="get">
			<div class="field">
				<label for="productname">Productname</label> <input type="text"
					name="productname" id="productname">
			</div>
			<div class="field">
				<label for="supplier">Supplier</label> <select name="supplier"
					id="supplier">
					<?php
					// Loading suppliers from database to options in HTML select menu
					$db = new PDO("pgsql:host=qwa.dk;port=5432;dbname=dbwa2","dbwa2","diku2012");
					$db->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);

					$stmt = $db->prepare("SELECT supplierid,companyname FROM nw_supplier ORDER BY companyname ASC");
					$stmt->execute();

					while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
						echo "<option value=\"{$row['supplierid']}\">{$row['companyname']}</option>\n";
					}
					?>
				</select>
			</div>
			<div class="field">
				<label for="category">Category</label> <select name="category"
					id="category">
					<?php
					// Loading categorys from database to options in HTML select menu
					$stmt = $db->prepare("SELECT categoryid,categoryname FROM nw_category ORDER BY categoryname ASC");
					$stmt->execute();

					while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
						echo "<option value=\"{$row['categoryid']}\">{$row['categoryname']}</option>\n";
					}
					?>
				</select>
			</div>
			<div class="field">
				<label for="quantityperunit">Quantity per unit</label> <input
					type="text" name="quantityperunit" id="quantityperunit">
			</div>
			<div class="field">
				<label for="unitprice">Unit price</label> <input type="text"
					name="unitprice" id="unitprice">
			</div>
			<input type="submit" value="Add" /> <input type="reset" value="Reset" />
		</form>
	</div>
	<?php
	/*
	 * If valid user input are present then execute the transaction
	*  to insert the new product.
	*/
	if (isset($_REQUEST["productname"]) && !empty($_REQUEST["productname"])&&
			strlen($_REQUEST["productname"]) <= 40 && strlen($_REQUEST["quantityperunit"]) <= 20 &&
			$_REQUEST["unitprice"] <= 170141183460469231731687303715884105727 &&
			isset($_REQUEST["quantityperunit"]) && !empty($_REQUEST["quantityperunit"]) &&
			isset($_REQUEST["unitprice"]) && is_numeric($_REQUEST["unitprice"]) && $_REQUEST["unitprice"] > -1 ) {

		$db->beginTransaction();

		$db->exec("LOCK TABLE nw_product IN SHARE MODE");

		// Could also use PostgreSQL's serial datatype but this is the SQL standard compliant method
		$stmt = $db->prepare("SELECT max(productid) from nw_product");
		$stmt->execute();
		$newProductId = $stmt->fetchColumn()+1;

		$stmt = $db->prepare("INSERT INTO nw_product (productid,productname,supplierid,categoryid,quantityperunit,unitprice,unitsinstock) VALUES (:productid,:productname,:supplierid,:categoryid,:quantityperunit,:unitprice,:unitsinstock)");
		$stmt->execute(array(":productid"=>$newProductId,":productname"=>$_REQUEST["productname"],":supplierid"=>$_REQUEST["supplier"],":categoryid"=>$_REQUEST["category"],":quantityperunit"=>$_REQUEST["quantityperunit"],":unitprice"=>$_REQUEST["unitprice"],":unitsinstock"=>0));

		$db->commit();
	}
	?>
	<div>
		<table border="1" cellspacing="0" cellpadding="5" width="100%">
			<thead>
				<tr>
					<td>ProductId</td>
					<td>Productname</td>
					<td>SupplierId</td>
					<td>CategoryId</td>
					<td>Quantityperunit</td>
					<td>Unitprice</td>
					<td>Unitinstock</td>
				</tr>
			</thead>
			<tbody>
				<?php

				//Connect to db and returning all products
				$stmt = $db->prepare("SELECT productid,productname,supplierid,categoryid,quantityperunit,unitprice,unitsinstock FROM nw_product ORDER BY productid desc");
				$stmt->execute();

				/*
				 *  As long as it is possible to fetch rows from $stmt
				*  then take one at a time and print it as a HTML row.
				*/
				while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){

					echo "<tr>";
					echo "<td>{$row['productid']}</td>";
					echo "<td>{$row['productname']}</td>";
					echo "<td>{$row['supplierid']}</td>";
					echo "<td>{$row['categoryid']}</td>";
					echo "<td>{$row['quantityperunit']}</td>";
					echo "<td>{$row['unitprice']}</td>";
					echo "<td>{$row['unitsinstock']}</td>";
					echo "</tr>\n";
				}
				?>
			</tbody>
		</table>
	</div>
</body>
</html>