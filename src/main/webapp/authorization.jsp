
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<style type="text/css">
			div {
	  			padding-top: 200px;
			}	
		</style>
		<title>Authorization</title>
	</head>
	<body>
		<div align="center">
			<h1>Authorization</h1>
			<br></br>
			<form method="post" action="${pageContext.request.contextPath}/?action=authorize" >
				<label for="login">Login</label>
				<input type="text" name="login" required placeholder="${login_placeholder}" pattern="[A-Za-z0-9_]{6,15}"
					title="Must contain 6-15 uppercase or lowercase letters or numbers or _." >
				<br></br>
				<label for="pass">Password</label>
				<input type="password" name="pass" required placeholder="${pass_placeholder}" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,15}" 
					title="Must contain at least 1 number and 1 uppercase and lowercase letter, and must be 6-15 symbols">
				<br></br>
    			<button type="submit">Enter</button>
			</form> 
			<form method="post" action="${pageContext.request.contextPath}/?action=get_reg">
				<button type="submit">Register</button>
			</form>
		</div>
	</body>
</html>