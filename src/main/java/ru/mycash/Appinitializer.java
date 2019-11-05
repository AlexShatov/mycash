package ru.mycash;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;

import ru.mycash.servlet.MyCashServlet;

public class Appinitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(AppCtxConfig.class);
		servletContext.addListener(new ContextLoaderListener(ctx));
		servletContext.addListener(new RequestContextListener());
		
		ServletRegistration.Dynamic registration = servletContext.addServlet("myCashServlet", new MyCashServlet());
		registration.setLoadOnStartup(1);
		registration.addMapping("/");
	}

}
