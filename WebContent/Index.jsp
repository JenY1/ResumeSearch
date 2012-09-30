<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Resume Search</title>
	<script type="text/javascript" src="scripts/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="style/bootstrap/bootstrap/js/bootstrap.min.js"></script>
	<link rel="stylesheet" type="text/css" href="style/bootstrap/bootstrap/css/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="style/search.css"/>
</head>
<body>
	<div id="page-container" class="wrapper">
		<div>
			<div class="main">
				<div class="search basic-search">
					<h1>Resume Search</h1>
					<form action="/Servlet/Search" method="GET">
						<input value name="q" type="text">
						<div>
							<input class="button btn primary-btn submit selected"
							tabindex="0" type="submit" value="Search"></input>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>