<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>CSE 3241 Phase V</title>
   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
   <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel = "stylesheet" type="text/css" href="stylesheet.css" media="screen" />
	<link href='https://fonts.googleapis.com/css?family=Roboto+Slab:700,400' rel='stylesheet' type='text/css'>
	<script src="phaseFiveJavascript.js" type="text/javascript" ></script>

</head>


<body>
	<div id="Input_Details">
		<div class="project">
			<h1>CSE 3241 Phase 5</h1>
			<h3>Select a Query to run.</h3>
			<div class="list">
				<ol>
					<li>
						SELECT fname, lname FROM CLIENT WHERE ssn IN (SELECT client_ssn FROM DEPENDENTS)
						UNION
						SELECT fname, lname FROM CLIENT WHERE employee_ssn IN (SELECT emp_ssn FROM EMPLOYEES WHERE lname = 'higgins')</li>
					<li>
						SELECT fname, lname FROM CLIENT WHERE ssn IN (SELECT client_ssn FROM DEPENDENTS)
						INTERSECT
						SELECT fname, lname FROM CLIENT WHERE employee_ssn IN (SELECT emp_ssn FROM EMPLOYEES WHERE lname = 'higgins')
					</li>
					<li>
						SELECT Account_ID FROM ACCOUNTS
						EXCEPT
						(SELECT ssn FROM CLIENT WHERE ssn IN (SELECT client_ssn FROM DEPENDENTS)
						INTERSECT
						SELECT employee_ssn FROM CLIENT WHERE employee_ssn IN (SELECT emp_ssn FROM EMPLOYEES WHERE lname = 'higgins'))
					</li>
					<li>
						SELECT vehicle_model FROM VEHICLES WHERE NOT EXISTS (SELECT Report_Number FROM ACCIDENT_HISTORY)
					</li>
					<li>
						SELECT SUM(salary) FROM EMPLOYEES WHERE salary > 40000
					</li>
					<li>
						SELECT * FROM (ACCOUNTS as a INNER JOIN CAR_INSURANCE_POLICY as c ON a.Account_ID = c.accountNumber) INNER JOIN BOAT_INSURANCE_POLICY as b ON b.accountNumber = a.Account_ID
					</li>
				</ol>
			</div>
				<br></br> Select Query to run:
				<div class="queryTextField">
					<select id="selectedQueryCode" name="queryCode">
						<option selected="selected" value = '0'>Select Query</option>
						<option value="1">Query 1 - Union</option>
						<option value="2">Query 2 - Intersect</option>
						<option value="3">Query 3 - Except</option>
						<option value="4">Query 4 - Division</option>
						<option value="5">Query 5 - Aggregate</option>
						<option value="6">Query 6 - Inner Join</option>
					</select>
				</div>
				<br></br> <input type="button" id='find' value='Search'
					onclick="sendRequestAndObtainResponse()">
			</div>
			<br></br>
			<div class ="ErrorMessage"></div>
		</div>
	</div>
	<br></br>
	<div id="contents">
	<div id="tableContents"></div>
</div>


	

</body>
</html>
