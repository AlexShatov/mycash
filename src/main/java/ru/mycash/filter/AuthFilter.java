package ru.mycash.filter;
 
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.mycash.domain.User;
 
@WebFilter("/*")
public class AuthFilter implements Filter {
	
	ArrayList<String> securedActions = new ArrayList<>();
 
    public AuthFilter() {
    }
 
    @Override
    public void destroy() {
    }
 
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String action = req.getParameter("action");
        User loginedUser = (User) request.getSession().getAttribute("loginedUser");
        if (("get_auth".equals(action) || "get_reg".equals(action)) & loginedUser != null) {
			response.sendRedirect(request.getContextPath() + "/?action=get_stats");
        } else if (securedActions.contains(action) & loginedUser == null) {
        	response.sendRedirect(request.getContextPath() + "/?action=get_auth");
        }else {
        	chain.doFilter(request, response);
        } 
    }
 
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    	securedActions.add("get_stats");
    	securedActions.add("get_cabinet");
    	securedActions.add("change_pass");
    	securedActions.add("get_counts");
    	securedActions.add("add_income");
    	securedActions.add("get_incomes");
    	securedActions.add("delete_income");
    	securedActions.add("get_expenses");
    }
 
}