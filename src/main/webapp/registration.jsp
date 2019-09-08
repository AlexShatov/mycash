
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<style type="text/css">
			div {
  				padding-top: 200px;
			}	
		</style>
		<title>Registration</title>
	</head>
	<body>
		<div align="center">
    		<h1>Registration</h1>
			<br></br>
			<form method="post" action="${pageContext.request.contextPath}/?action=register">
				<label for="login">Login</label>
				<input type="text" name="login" placeholder="${login_placeholder}" pattern="[A-Za-z0-9_]{6,15}"
					title="Must contain 6-15 uppercase or lowercase letters or numbers or _." required>
				<br></br>
				<label for="pass">Password</label>
				<input type="password" name="pass" placeholder="${pass_placeholder}" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,15}" 
					title="Must contain at least 1 number and 1 uppercase and lowercase letter, and must be 6-15 symbols" required>
				<br></br>
				<label for="email">E-mail</label>
				<input type="email" name="email" placeholder="${mail_placeholder}" 
					pattern="([a-zA-Z0-9!#$%&amp;'*+\/=?^_`{|}~.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*){6,25}" 
					title="Must be shorter than 25 symbols" required>
				<br></br>
    			<button type="submit">Submit</button> 
			</form> 
	</div>
	</body>
</html>