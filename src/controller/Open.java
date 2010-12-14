package controller;

import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import servlet.AjaxModuleListServletProvider;

public class Open {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		URL referenceURL = Open.class.getClassLoader().getResource("old_index.html");
		//System.out.println( referenceURL.toString() );
		
		// Create a local jetty server		
		Server server = new Server( 80 );
		 
        ServletContextHandler servletContextHandler = new ServletContextHandler( ServletContextHandler.SESSIONS );
        servletContextHandler.setContextPath( "/pipes" );
        server.setHandler( servletContextHandler );
        servletContextHandler.addServlet( new ServletHolder( new AjaxModuleListServletProvider() ), "/ajax.module.list" );
       
        //WebAppContext webapp = new WebAppContext();
        //webapp.setContextPath("/web");
        //TODO: add web app war file
        //webapp.setWar(referenceURL.getPath()+"/webapps/test.war");
 
        //ContextHandlerCollection contexts = new ContextHandlerCollection();
       // contexts.setHandlers(new Handler[] { servletContextHandler, webapp });
        //server.setHandler(contexts);
        
        server.setHandler( servletContextHandler );
 
        server.start();
        OpenBrowser.openURL( referenceURL.toExternalForm() );
        server.join();
	}
}
