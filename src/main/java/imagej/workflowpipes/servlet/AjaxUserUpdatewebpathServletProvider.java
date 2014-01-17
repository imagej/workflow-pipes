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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * ajax.user.updatewebpath takes four inputs through the post; path, _rnd, .crumb, and _out
 * and returns a string message such as {"ok":0,"message":"Update failed: 'httppipesyahoocompipespersoninfo' is not allowed"} for failure
 * or {"ok":1,"data":"http:\/\/localhost\/web\/person.info?guid=SEW2J7WHQTWXX464XNNHQCOAQE"} for success
 * 
 * An example of a successful post is: 
path:/web/person.info
_rnd:5021
.crumb:9Dxl9FJn/wN
_out:json
 * 
 */
public class AjaxUserUpdatewebpathServletProvider extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1458365345236667188L;
	private JSONObject json = new JSONObject();
	private PipesController pipesController;

	public AjaxUserUpdatewebpathServletProvider( PipesController pipesController ) {
		this.pipesController = pipesController;
	}

	protected void doGet( HttpServletRequest request,
			HttpServletResponse response ) throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		// get the path
		String path = request.getParameter("path");
		
		// get the random number
		String rnd = request.getParameter("_rnd");
		
		// get the .crumb
		String crumb = request.getParameter(".crumb");
		
		// get the output format
		String out = request.getParameter("_out");
		
		// TODO: add rnd and .crumb checks

		// get the response, a string containing the same id
		json = this.pipesController.userUpdatewebpath( path, rnd, out, json, crumb );

		// generate and send the response
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write( json.toString() );
	}
}
