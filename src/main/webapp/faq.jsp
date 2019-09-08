<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Stats</title>
	</head>
	<body>
		<header>
			<table width="100%" bgcolor="#f0f0f0">
				<tr valign="middle">
					<td align="center" height="82" width="82">
						<img src="/img/logo.png" alt="logo" width="80" height="80">
					</td>
					<td align="right">
						<a href="${pageContext.request.contextPath}/?action=get_cabinet" class="href">${login}</a>
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
 					<h1>FAQ</h1>
 					<br></br>
 					
				</td>	
			</tr>	 
		</table>	
	</body>
</html>