/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2011 - 2014 Board of Regents of the University of
 * Wisconsin-Madison.
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
 * #L%
 */

package imagej.workflowpipes.servlet;

import imagej.workflowpipes.controller.PipesController;
import imagej.workflowpipes.pipesapi.Module;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class AjaxModuleListServletProvider extends HttpServlet {

	private static final long serialVersionUID = 1458365345236667188L;
	private JSONObject json = new JSONObject();

	public AjaxModuleListServletProvider( PipesController pipesController ) 
	{
		JSONArray jsonArrayModule = new JSONArray();

		//add each module to the array
                Module outputPipesModule = null;
		for( Module pipesModule : pipesController.getModulesServiceHashMap().values() )
		{
                    System.out.println("pipesModule " + pipesModule.getID().getValue());
                    if ("_OUTPUT".equals(pipesModule.getID().getValue())) {
                        outputPipesModule = pipesModule;
                    }
                    else {
			//Get the JSONObject representative of the module entity
			JSONObject jsonModule = pipesModule.getJSONObject();

                        System.out.println("jsonModule " + jsonModule.toString());
			
			//put the module in the array
			jsonArrayModule.put( jsonModule );
                    }
		}
                if (null != outputPipesModule) {
                    jsonArrayModule.put( outputPipesModule.getJSONObject() );
                }
		
		// add module to JSONObject
		json.put("module", jsonArrayModule);
		
		// Add get 1 to array
		json.put( "ok", new Integer(1) );

	}

	/**
	 * input parameters are 	_out=json
	 * 							rnd=5582
	 * 							.crumb=RqBrtsZBJKP
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
            System.out.println("doGet " + json.toString());
		
		 
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(json.toString());
	}

}
