package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import servletutils.ServletRequestHelper;

public class AjaxPipePreviewServletProvider extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1458365345236667188L;
	private JSONObject json = new JSONObject();

	public AjaxPipePreviewServletProvider() {
	
	}

	protected void doPost( HttpServletRequest request,
			HttpServletResponse response ) 
	{
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
	
		//return the post
		try {
			//TODO:finish post the object
			String jsonString = ServletRequestHelper.getResponseFiled( "def", request ).toString();
			response.getWriter().write( jsonString );
			System.out.println( jsonString );
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("doPOST called ");
	}

	protected void doGet( HttpServletRequest request,
			HttpServletResponse response ) throws ServletException, IOException {

		doPost( request, response );
	}

}
