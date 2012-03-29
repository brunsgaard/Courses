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
</style>

</head>

<body>

	<h1>Search for employees</h1>
	<div class="centered">
		<form name="input" action="problem2_1.php" method="get">
			<input type="text" name="query" /> <input type="submit"
				value="Search" />
		</form>
	</div>

	<div>
		<table border="1" cellspacing="0" cellpadding="5" width="100%">
			<thead>
				<tr>
					<td>Employeeid</td>
					<td>Lastname</td>
					<td>Firstname</td>
					<td>Title</td>
					<td>Birthday</td>
					<td>Hiredate</td>
					<td>Address</td>
					<td>City</td>
					<td>Postalcode</td>
					<td>Country</td>
				</tr>
			</thead>
			<tbody>
				<?php
					
				// Creating the PDO object with the connection to the db
				$db = new PDO("pgsql:host=qwa.dk;port=5432;dbname=dbwa2","dbwa2","diku2012");
				$db->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);



				// Preparing and executing query based on whether or not user input are present	
				if (isset($_REQUEST["query"]) && !empty($_REQUEST["query"])){
					$stmt = $db->prepare("SELECT employeeid,lastname,firstname,title,EXTRACT(EPOCH FROM birthdate ) AS birthdateepoch,EXTRACT(EPOCH FROM hiredate ) AS hiredateepoch,address,city,postalcode,country FROM nw_employee WHERE firstname ILIKE :query OR  lastname ILIKE :query ORDER BY lastname,firstname");
					$stmt->execute(array(":query"=>"%".$_REQUEST["query"]."%"));
				} else{
					$stmt = $db->prepare("SELECT employeeid,lastname,firstname,title,EXTRACT(EPOCH FROM birthdate ) AS birthdateepoch,EXTRACT(EPOCH FROM hiredate ) AS hiredateepoch,address,city,postalcode,country FROM nw_employee ORDER BY lastname,firstname");
					$stmt->execute();
				}

				/*
				 *  As long as it is possible to fetch rows from $stmt
				 *  then take one at a time and print it as a HTML row.
				 */
				while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){

					echo "<tr>";
					echo "<td>{$row['employeeid']}</td>";
					echo "<td>{$row['lastname']}</td>";
					echo "<td>{$row['firstname']}</td>";
					echo "<td>{$row['title']}</td>";
					echo "<td>".date("d-M-y",$row['birthdateepoch'])."</td>";
					echo "<td>".date("d-M-y",$row['hiredateepoch'])."</td>";
					echo "<td>{$row['address']}</td>";
					echo "<td>{$row['city']}</td>";
					echo "<td>{$row['postalcode']}</td>";
					echo "<td>{$row['country']}</td>";
					echo "</tr>\n";
				}
				?>
			</tbody>
		</table>
	</div>
</body>
</html>


