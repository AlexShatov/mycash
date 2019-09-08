<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Cabinet</title>
	</head>
	<body>
		<header>
			<table width="100%" bgcolor="#f0f0f0">
				<tr valign="middle">
					<td align="center" height="82" width="82">
						<img src="/img/logo.png" alt="logo" width="80" height="80">
					</td>
					<td align="right">
						<p>${login}</p>
						<form method="post" action="${pageContext.request.contextPath}/?action=logout">
							<button type="submit">Logout</button>
						</form>
					</td>
				</tr>
			</table>
		</header>
		<table width="100%">
			<tr valign="top">
				<td align="left" width="200" bgcolor="#f0f0f0">
					<ul>
					<li><a href="${pageContext.request.contextPath}/?action=get_stats">Statistics</a></li>
    				<li><a href="${pageContext.request.contextPath}/?action=get_incomes">Incomes</a></li>
   					<li><a href="${pageContext.request.contextPath}/?action=get_expenses">Expenses</a></li>
    				<li><a href="${pageContext.request.contextPath}/?action=get_counts">Counts</a></li>
    				<li><a href="${pageContext.request.contextPath}/?action=get_budget">Budget</a></li>
    				<li><a href="${pageContext.request.contextPath}/?action=get_cabinet">User's cabinet</a></li>
    				<li><a href="${pageContext.request.contextPath}/?action=get_faq">FAQ</a></li>
					</ul>
				</td>
				<td align="left">
 					<h1>User's cabinet</h1>
 					<h3>Change password:</h3>
 					<form method="post" action="${pageContext.request.contextPath}/?action=change_pass">
						<label for="old_pass">Old password</label>
						<input type="password" name="old_pass" placeholder="${old_pass_placeholder}" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,15}"
							title= "Must contain at least 1 number and 1 uppercase and lowercase letter, and must be 6-15 symbols" required>
						<br></br>
						<label for="new_pass">New password</label>
						<input type="password" name="new_pass" placeholder="${new_pass_placeholder}" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,15}" 
							title="Must contain at least 1 number and 1 uppercase and lowercase letter, and must be 6-15 symbols" required>
						<br></br>
						<label for="repeat_new_pass">Repeat new password</label>
						<input type="password" name="repeat_new_pass" placeholder="${rep_pass_placeholder}" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,15}"
							title="Must contain at least 1 number and 1 uppercase and lowercase letter, and must be 6-15 symbols" required>
						<br></br>
    					<button type="submit">Change</button> 
					</form>
				</td>	
			</tr>	 
		</table>	
	</body>
</html>