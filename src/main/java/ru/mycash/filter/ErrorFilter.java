package ru.mycash.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@WebFilter("/*")
public class ErrorFilter implements Filter {
	
 
    public ErrorFilter() {
    }
    
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }
 
    @Override
    public void destroy() {
    }
 
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        ServletException exc = (ServletException)request.getSession().getAttribute("listenerException");
        if(exc != null) {
        	throw exc;
        }else {
        	chain.doFilter(request, response);
        }
    }
       
 

 
}