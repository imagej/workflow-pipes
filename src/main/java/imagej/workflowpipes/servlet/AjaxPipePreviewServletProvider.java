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

package imagej.workflowpipes.servlet;

import imagej.workflowpipes.controller.PipesController;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class AjaxPipePreviewServletProvider extends HttpServlet {

	private static final boolean DEBUG = false;
	private static final long serialVersionUID = -1156366721753088035L;
	private PipesController pipesController;
	
	public AjaxPipePreviewServletProvider( PipesController pipesController  ) 
	{	
		
		if (DEBUG) System.out.println( "Servlet provider constructed");
		//maintain a reference to the pipes controller
		this.pipesController = pipesController;
	}

	protected void doPost( HttpServletRequest request,
			HttpServletResponse response ) throws ServletException, IOException {
		
		System.out.println( "Servlet provider :: do post called");
		
		// Evaluate the inputs
		String definitionString = request.getParameter( "def" );
		
		System.out.println( "Servlet provider :: def is " + definitionString);
		
		JSONObject defJSON = null;
		try {
			defJSON = new JSONObject( definitionString );
		} catch (ParseException e) {
			e.printStackTrace();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write( request.getParameter( "def" ) );
			return;
		}
		
		// Have the pipes controller process the results
		JSONObject responseJSON = pipesController.evaluate( defJSON );
			
		System.out.println( "Servlet provider :: post evalute JSON is " + responseJSON );
		
		String output =  responseJSON.toString();
		
		// generate and send the response
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write( output );
	}

	protected void doGet( HttpServletRequest request, HttpServletResponse response )
	throws ServletException, IOException {
		doPost( request, response );
	}

}
