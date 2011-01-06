package pipesapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import pipesentity.Conf;
import pipesentity.Count;
import pipesentity.Description;
import pipesentity.Duration;
import pipesentity.Error;
import pipesentity.ID;
import pipesentity.Item;
import pipesentity.ItemCount;
import pipesentity.Message;
import pipesentity.Name;
import pipesentity.Preview;
import pipesentity.Prop;
import pipesentity.Response;
import pipesentity.Result;
import pipesentity.Source;
import pipesentity.Start;
import pipesentity.Tag;
import pipesentity.Terminal;
import pipesentity.Type;
import pipesentity.UI;

public abstract class Module {
	
	public abstract void go();
	
	// the errors reported by the object
	protected ArrayList<Error> errors = new ArrayList<Error>();
	
	// the count of the module
	protected Count count = new Count( 0 );
	
	// the number of items in the module
	protected ItemCount item_count = new ItemCount();
	
	// the unique identifier used to differentiate 
	// a local instance of a module
	protected ID id;
	
	// a reference to the instance of module
	// this can be the classpath or other reference
	// TODO: refine this further
	protected String module;
	
	// a list used to store the module's items
	protected ArrayList<Item> items = new ArrayList<Item>();
	
	// a list used to store the inputs and outputs of a module
	protected ArrayList<Terminal> terminals = new ArrayList<Terminal>();
	
	// the markup language used to create the module's UI
	protected UI ui;
	
	// the text name of the module
	protected Name name;
	
	// the type of module
	protected Type type;
	
	// discoverable text conveying understanding of the module
	protected Description description;
	
	// discoverable terms used to discover this module
	protected ArrayList<Tag> tags = new ArrayList<Tag>();
	
	// list of the configuration properties unique to a module
	protected ArrayList<Conf> confs = new ArrayList<Conf>();
	
	// used to track the total execution time of the module execution
	protected Duration duration = new Duration( 0.0 );
	
	// used to track the start time of module execution
	protected Start start = new Start( 0L );
	
	// response used to track the modules performance stats
	private Response response = new Response();
	
	// used to store the execution errors
	protected HashMap< Type, Message > moduleErrors = new HashMap< Type, Message>();
	
	// used to store the Preview
	protected Preview preview;
	
	// Props list
	protected ArrayList<Prop> props = new ArrayList<Prop>();
	
	/**
	 * Called at the beginning of getPreview()
	 */
	protected void start()
	{
		this.start = new Start( System.currentTimeMillis() );
	}
	
	/**
	 * Called at the end of getPreview()
	 */
	protected void stop()
	{
		this.duration = new Duration( System.currentTimeMillis() - start.getValue() );
		
		// runs the execution stats
		runStats();
	}
	
	public ArrayList<Error> getErrors()
	{
		// return the errors
		return errors;
	}

	// internal method used for generating runtime stats
	private void runStats()
	{
		response.run( duration );
	}
	
	// add an error to the module
	public void addError( Error error )
	{
		errors.add( error );
	}
	
	protected void addErrorWarning( String warningString ) 
	{
		errors.add( new Error( new Type( "warning" ), new Message ( warningString ) ) );
		
	}

	public void assignInstanceValues( JSONObject jsonObject ) 
	{	
		if( jsonObject != null )
		{
			// if id is defined
			if (jsonObject.getString("id") != null) {
				this.id = new ID(jsonObject.getString("id"));
			}

			// if type is defined
			if (jsonObject.getString("type") != null) {
				this.type = new Type(jsonObject.getString("type"));
			}

			// if conf is defined
			if (jsonObject.getString("conf") != null) {
				this.confs = Conf.getConfs(jsonObject.getString("conf"));
			}
		}
	}

	public ArrayList<Terminal> getTerminals() {
		return terminals;
	}

	public UI getUI() {
		return ui;
	}

	public Description getDescription() {
		return description;
	}

	public Type getType() {
		return type;
	}

	public Name getName() {
		return name;
	}

	public ArrayList<Tag> getTags() {
		return tags;
	}

	public JSONObject getJSONObject() {

		JSONObject jsonModule = new JSONObject();
		JSONArray jsonArrayTerminals = new JSONArray();

		if (getID() != null) {
			// add ui key value to the object
			jsonModule.put( "ui", getID().getValue() );
		}
		
		if (getTerminals() != null) {
			// add the terminals
			for (Terminal terminal : getTerminals()) {
				JSONObject jsonObjectTerminals = new JSONObject();
				jsonObjectTerminals.put(terminal.getTypeKey(),
						terminal.getTypeValue());
				jsonObjectTerminals.put(terminal.getDirectionKey(),
						terminal.getDirectionValue());
				jsonArrayTerminals.put(jsonObjectTerminals);
			}

			// add terminals array to module
			jsonModule.put("terminals", jsonArrayTerminals);
		}

		if (getUI() != null) {
			// add ui key value to the object
			jsonModule.put( "ui", getUI().getValue() );
		}

		if (getName() != null) {
			// add name to module
			jsonModule.put("name", getName().getValue() );
		}

		if (getType() != null) {
			// add type to module
			jsonModule.put("type", getType().getValue() );
		}

		if (getDescription() != null) {
			// add description to module
			jsonModule.put("description", getDescription().getValue() );
		}

		if (getTags() != null) {
			// create an array for the tags
			JSONArray jsonArrayTags = new JSONArray();

			// add tags to array
			for (Tag tag : getTags())
				jsonArrayTags.put(tag.getValue());

			// add tags array to module
			jsonModule.put("tags", jsonArrayTags);
		}

		return jsonModule;
	}

	public ID getID() {
		return this.id;
	}

	/**
	 * Performs a string literal match on the types
	 * 
	 * @param type
	 * @return
	 */
	public boolean matchesType(String type) {
		if (this.type.getValue().equals(type)) {
			return true;
		}
		return false;
	}

	public boolean searchNameAndDescription(String searchTerm) {
		if (this.name.getValue().contains(searchTerm) // or
				|| this.description.getValue().contains(searchTerm)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the Result representation of the module
	 * 
	 * @return
	 */
	public Result getResult() {
		// create a Result from the module
		Result result = new Result(this.name, this.description, this.type,
				new Source("pipe"));

		// return the result
		return result;
	}



	/**
	 * 
	 * @param modulesArrayList
	 * @returns the key value is module type - for ease of searching
	 */
	public static HashMap<Type, Module> getHashMap( ArrayList<Module> modulesArrayList ) 
	{
		HashMap< Type, Module > modulesHashMap = new HashMap<Type,Module>();
		
		// use an Iterator to cycle over the modules
		Iterator<Module> modulesIterator = modulesArrayList.iterator();
		
		// while there are modules
		while( modulesIterator.hasNext())
		{
			// get the module
			Module module = (Module) modulesIterator.next();
			
			//add the module to the HashMap
			modulesHashMap.put( module.getType(), module );
		}
		
		//return the resulting list
		return modulesHashMap;
	}
	
	/**
	 * Returns the Modules as an ArrayList
	 * @param moduleHashMap
	 * @return
	 */
	public static ArrayList<Module> getModulesArrayList( HashMap<Type, Module> moduleHashMap) {
		ArrayList<Module> moduleArrayList = new ArrayList<Module>();
		
		for( Module module :moduleHashMap.values() )
		{
			//add the module
			moduleArrayList.add( module );
		}
		
		return moduleArrayList;
	}

	/**
	 * Given a list of input Modules, return the first matching moduleid
	 * @param moduleid
	 * @param modules
	 * @return
	 */
	public static Module getModuleByID( String moduleid, ArrayList<Module> modules ) 
	{
		//get an Iterator
		Iterator<Module> iterator = modules.iterator();
		
		//find the match
		while( iterator.hasNext() )
		{
			//get the next iterator
			Module module = iterator.next();
			
			//if ids match
			if( module.id.getValue() == moduleid )
				return module;
		}
		
		return null;
	}

	public ArrayList<Conf> getConfArray() {
		return confs;
	}

	public JSONObject getPreviewJSON() 
	{
		JSONObject json = new JSONObject();
		
		// add the count
		json.put("count", count.getValue() );
		
		// add the item count
		json.put("item_count", item_count.getValue() );
		
		// add the duration
		json.put("duration", duration.getValue() );
		
		// add the prop
		json.put( "prop", Prop.getJSON( props ) );
		
		// add the module id
		json.put( "id", id.getValue() );
		
		// add the items
		json.put( "items", Item.getJSON( items ) );
		
		// add the module
		json.put("module", module );
		
		// add the start time
		json.put("start", start.getValue() );
		
		return json;
	}

}
