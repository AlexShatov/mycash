<%@ page import="java.util.ArrayList,ru.mycash.domain.Count,ru.mycash.domain.Expense,ru.mycash.domain.ExpenseCategory,ru.mycash.dao.MySqlCountDao,ru.mycash.dao.MySqlExpenseCategoryDao,org.springframework.context.ApplicationContext, org.springframework.web.servlet.support.RequestContextUtils" %>
<!DOCTYPE html>
<html>
  <head>
	<meta charset="ISO-8859-1">
	<title>Incomes</title>
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
		  <form method="post" action="${pageContext.request.contextPath}/?action=add_expense">
			<table>
			  <tr>
				<td align="left">
				  <h1>Expenses</h1>
				</td>
			  </tr>
			  <tr>
				<td align="left" width="180">
				  <label for="annotation">Annotation</label>
				</td>
				<td align="left" width="180">
				  <label for="category">Category</label>
				</td>
				<td align="left" width="180">
				  <label for="count">Count</label>
				</td>
				<td align="left" width="180">
				  <label for="amount">Amount</label>				
				</td>
				<td align="left" width="180">
				  <label for="date">Date</label>
				</td>
			  </tr>	
			  <tr>
				<td align="left" width="180">
				  <input type="text" name="annotation" placeholder="${annotation_holder}" pattern="[\d+\w+\s+]{1,20}"
					title="Must contain numbers or uppercase or lowercase latin letters and spaces" required>
				</td>
				<td>
				  <select name="category">
					<%
						ArrayList<ExpenseCategory> categories = (ArrayList<ExpenseCategory>)request.getAttribute("categories");
						for(int i=0; i < categories.size(); i++){
							out.println("<option>" + categories.get(i).getCategoryName() + "</option>");
						}
					%>
				  </select>
				</td>
				<td>
				  <select name="count">
					<%
					ArrayList<Count> counts = (ArrayList<Count>)request.getAttribute("counts");
					for(int i = 0; i < counts.size(); i++){
						out.println("<option>" + counts.get(i).getCountName() + "</option>");
					}%>
				  </select>
				</td>
				<td>
				  <input type="text" name="amount" placeholder="${amount_holder}" pattern="\d+(\.\d{1,2})?" required>
				</td>
				<td>
				  <input type="date" name="date" required>
				</td>
				<td>
				  <button type="submit">Add</button>
				</td>
			  </tr>
			</table>
		  </form>
		  <form method="post" action="${pageContext.request.contextPath}/?action=get_expenses">  
			<table>
			  <tr>
				<td align="left" width="100">
				  <p>Period:</p>
				</td>
				<td align="left" width="100">
				  <input type="date" name="start_date">
				</td>
				<td align="left" width="100">
				  <input type="date" name="end_date">
				</td>
				<td>
				  <button type="submit">Select</button>
				</td>
			  </tr>
			</table>
		  </form>
			<table>    
			  <tr>
			    <%
			    ArrayList<Expense> expenses = (ArrayList<Expense>)request.getAttribute("expenses");
			    ApplicationContext ac = RequestContextUtils.findWebApplicationContext(request);
			    MySqlCountDao countDao = (MySqlCountDao)ac.getBean("countdao");
			    MySqlExpenseCategoryDao categoryDao = (MySqlExpenseCategoryDao)ac.getBean("expensecatdao");
			    out.println("<table>");
			    for(int i=0; i<expenses.size(); i++){
			    	int countId = expenses.get(i).getCount().getId();
			    	int categoryId = expenses.get(i).getExpenseCategory().getId();
			    	String countName = countDao.read(countId).getCountName();
			    	String categoryName = categoryDao.read(categoryId).getCategoryName();
			    	out.println("<tr><td align=\"left\" width=\"180\">" + expenses.get(i).getAnnotation() + "</td>" +
			    			"<td align=\"left\" width=\"180\">" + categoryName + "</td>" +
			    			"<td align=\"left\" width=\"180\">" + countName + "</td>" + 
			    			"<td align=\"left\" width=\"180\">" + expenses.get(i).getAmount() + "</td>" + 
			    			"<td align=\"left\" width=\"180\">" + expenses.get(i).getExpenseDate() + "</td>" +
			    			"<td align=\"left\" width=\"100\"><form method=\"post\" action=\"${pageContext.request.contextPath}/?action=delete_expense&expense_id=" +
							expenses.get(i).getId() + "\"><button type=\"submit\">Delete</button></form></td></tr>"
			    			);
			    }
			    out.println("</table>");
			    %>
			  </tr>
			</table>	
		</td>	
	  </tr>	 
	</table>	
  </body>
</html>