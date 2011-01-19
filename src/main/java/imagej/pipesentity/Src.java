package imagej.pipesentity;

import org.json.JSONObject;

public class Src {
	
	private String moduleid;
	private String id;

	public Src( String id, String moduleid )
	{
		this.id = id;
		this.moduleid = moduleid;
	}
	
	public Src( JSONObject json ) {
		this.id = json.getString("id");
		this.moduleid = json.getString("moduleid");
	}

	public JSONObject getJSONObject()
	{
		JSONObject json = new JSONObject();
		
		//populate the values
		json.put( "id", id );
		json.put( "moduleid", moduleid );
		
		return json;
	}

	public String getModuleid() {
		return moduleid;
	}

	public String getId() {
		return id;
	}
}
