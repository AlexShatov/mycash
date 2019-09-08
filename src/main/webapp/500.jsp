
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Counts</title>
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
		<h1>Internal server error</h1>
		<h3>${err_message}</h3>
	</body>
</html>