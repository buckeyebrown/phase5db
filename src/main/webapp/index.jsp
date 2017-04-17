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
			<h1>Our group's project for CSE 3241.</h1>

				<br></br> Select Query to run:
				<div class="queryTextField">
					<select id="selectedQueryCode" name="queryCode">
						<option selected="selected" value = '0'>Select Query</option>
						<option value="1">Query 1</option>
						<option value="2">Query 2</option>
						<option value="3">Query 3</option>
						<option value="4">Query 4</option>
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
