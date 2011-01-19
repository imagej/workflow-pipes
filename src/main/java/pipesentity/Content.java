package pipesentity;

import org.json.JSONObject;

public class Content {
	private Type type;
	private Count count;
	
	public Content( Type type, Count count )
	{
		this.type = type;
		this.count = count;
	}
	

	public Type getType()
	{
		return type;
	}
	
	public Count getCount()
	{
		return count;
	}
	
	public JSONObject getJSON()
	{
		JSONObject json = new JSONObject();
		
		json.put("_type", type.getValue() );
		json.put("_count", count.getValue() );
		
		return json;
	}
}
