package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pipesentity.Module;
/**
 * Does a Post with the following parameters:
 * _out (json), query (text), rnd (6730), and .crumb(8C/LW9MBuRW)
 * 
 * @author ipatron returns the top 15 results in JSON with url, name, description, source (for source:"feed")
 * or name, description, type(E.g. type: "pipe:nHNB8TJm3BGumlGA9YS63A"), and source  (for source: "pipe")
 *
 */
public class AjaxFeedFindServletProvider extends HttpServlet {

	private static final long serialVersionUID = 1458365345236667188L;
	private JSONObject json = new JSONObject();
	private ArrayList<Module> pipesArrayList;

	public AjaxFeedFindServletProvider(ArrayList<Module> pipesArrayList) {
		
		this.pipesArrayList = pipesArrayList;
		
		// Add get 1 to array
		//json.put("ok", new Integer(1));

		// System.out.println( json.toString() );

	}

	protected void doPOST( HttpServletRequest request,
			HttpServletResponse response ) throws ServletException, IOException {
		
		//get the query text
		String query = request.getParameter( "query" );
		
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write( json.toString() );
		
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		
		
		// response.getWriter().println( "session=" +
		// request.getSession(true).getId());
	}

}
