/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2009 - 2012 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */

package imagej.workflowpipes.controller;

import imagej.workflowpipes.util.OpenBrowser;

import java.util.HashMap;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Helper class to control multiple servlet instances within a single controller
 * object.
 * 
 * @author loci
 * 
 */
public class JettyServerController {

	private Server jettyServer;
	private int jettyServerPort = 1999;
	private String contextPathString;
	private String startPageString;
	private boolean jettyDirectoryListed = false;
	private HashMap<String, Servlet> servletInstanceHashMap = new HashMap<String, Servlet>();

	/**
	 * Creates an instance of the jettyServer on the portJettyServer
	 * 
	 * @param jettyServerPort
	 *            - the suggested port for the service
	 */
	public JettyServerController( int jettyServerPort, String startPageString, String contextPathString, boolean jettyDirectoryListed ) 
	{
		// set the start page string
		if (startPageString != null)
			this.startPageString = startPageString;

		// set the context path string
		if (contextPathString != null)
			this.contextPathString = contextPathString;
		
		// set the directory listing property
		this.jettyDirectoryListed = jettyDirectoryListed;

		// set the jetty operating port
		this.jettyServerPort = jettyServerPort;

		// bounds check and assign the input port
		if ( jettyServerPort < 65536 && jettyServerPort > 1000) {
			this.jettyServerPort = jettyServerPort;
		}

		// create a new jetty server instance
		jettyServer = new Server();

		// Set up a channel selector object
		SelectChannelConnector connector = new SelectChannelConnector();

		// set the port number
		connector.setPort( this.jettyServerPort );

		// add the connector to the server
		jettyServer.addConnector( connector );

	}

	public void setHandler(ContextHandlerCollection contexts) {
		jettyServer.setHandler(contexts);
	}

	public void startAndLaunchBrowser() throws Exception {
		// Create the resource handler
		ResourceHandler resource_handler = new ResourceHandler();

		// disallow directory listing
		resource_handler.setDirectoriesListed( this.jettyDirectoryListed );

		// Set the initial load file
		resource_handler.setWelcomeFiles(new String[] { this.startPageString });

		// point to the local directory
		resource_handler.setResourceBase(".");
		
		// use sessions
		ServletContextHandler servletContextHandler = new ServletContextHandler(
				ServletContextHandler.SESSIONS);

		// set /web as the default path
		servletContextHandler.setContextPath( this.contextPathString );

		// add the handler
		jettyServer.setHandler( servletContextHandler );
		
		// get the servlets
		for (String servletPath : servletInstanceHashMap.keySet()) {
			// add a servlet
			servletContextHandler.addServlet(new ServletHolder(
					servletInstanceHashMap.get( servletPath )), servletPath);
		}

		// create a handler list
		HandlerList handlers = new HandlerList();

		// add three handlers
		handlers.setHandlers( new Handler[] { servletContextHandler,
				resource_handler, new DefaultHandler() });

		// pass the handlers to the server
		jettyServer.setHandler(handlers);
		
		// start the session
		jettyServer.start();

		// Open the users browser to the default page
		//for authenticated (E.g. OpenID check)  OpenBrowser.openURL( "http://workflowpipes.appspot.com/_ah/login?continue=http://localhost:" + this.jettyServerPort + this.contextPathString ); //Why does this not work with default values? (E.g. index.html): + "/" + this.startPageString ); A: This needs to 
		OpenBrowser.openURL( "http://localhost:" + this.jettyServerPort + this.contextPathString + "/"  );
	}

	public void stop() throws Exception {
		jettyServer.stop();
		jettyServer.join();
	}

	public boolean isStarted() {
		return jettyServer.isStarted();
	}

	public boolean isStopped() {
		return jettyServer.isStopped();
	}
	
	public void addServlet( String servletPath, Servlet servlet )
	{
		servletInstanceHashMap.put( servletPath, servlet);
	}
	
	public void removeServlet ( String servletPath )
	{
		servletInstanceHashMap.remove( servletPath );
	}

}
