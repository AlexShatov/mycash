
<%@ page import="java.util.ArrayList,ru.mycash.domain.Count" %>
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
 		  <h1>Counts</h1>
		  <br></br>
		  <form method="post" action="${pageContext.request.contextPath}/?action=add_count">
 			<table width="500">
 			  <tr valign="middle">
 				<td align="left" width="180">
 				  <label for="count_name">Name</label> 
 				</td>
 				<td align="left" width="180">
 				  <label for="balance">Amount</label> 
 				</td>
 				<td align="left" width="180">
 				  <label for="currency">Currency</label> 
 				</td>
 			  </tr>
 			  <tr>	
 				<td align="left" width="180">
 				  <input type="text" name="count_name" placeholder="${count_name_holder}" pattern="[A-Za-z0-9_]{1,15}"
				  title="Must contain 6-15 uppercase or lowercase letters or numbers or _." required>
 				</td>
 				<td align="left" width="180">
 				  <input type="text" name="balance" placeholder="${balance_holder}" pattern="\d+(\.\d{1,2})?"
				  title="Number format is x.xx" required>
 				</td>
 				<td align="left" width="100">
 				  <select  name="currency">
 					<option>BYN</option>
 					<option>RUB</option>
 					<option>USD</option>
 					<option>EUR</option>
 					<option>JPY</option>
 					<option>CNY</option>
 					<option>CHF</option>
 					<option>UAH</option>
 					<option>GBP</option>
 				  </select>
 				</td>
 				<td align="left">
 				  <button type="submit">Add</button>
 				</td>		
 			  </tr>
 				<%
 				ArrayList<Count> counts = (ArrayList<Count>)request.getAttribute("counts");
 				for(int i = 0; i < counts.size(); i++){
 				out.println("<tr><td align=\"left\" width=\"180\">" + counts.get(i).getCountName() + "</td>" + 
 						"<td align=\"left\" width=\"180\">" + counts.get(i).getBalance()+ "</td>" + 
 						"<td align=\"left\" width=\"100\">" + counts.get(i).getCurrency() + "</td>" + 
 						"<td align=\"left\" width=\"100\"><form method=\"post\" action=\"${pageContext.request.contextPath}/?action=delete_count&count_id=" +
 						counts.get(i).getId() + "\"><button type=\"submit\">Delete</button></form></td></tr>");
 				}
 				%>
 			</table>
		  </form>
		</td>	
	  </tr>	 
	</table>	
  </body>
</html>