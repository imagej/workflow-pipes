package experimental;

import java.util.ArrayList;
import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONObject;

import pipes.ModuleGenerator;
import pipes.Service;
import pipesapi.Module;
import pipesentity.Def;
import pipesentity.Preview;
import workflow.LayoutToWorkFlow;
public class LocalDefEvaluator {

	static final boolean DEBUG = true;
	
	public static JSONObject getPreview( Def def, HashMap<Service, Module> moduleHashMap )
	{	
		if (DEBUG) System.out.println(" LocalDefEvaluator :: getPreview ... starting array list generation from def ");
		
		JSONArray modulesJSONArray = def.getModulesArray();
			
		ArrayList<Module> moduleList = new ArrayList<Module>();

		for (int i = 0; i < modulesJSONArray.length(); i++) 
		{
			//get the JSONObject
			JSONObject jsonInternal = modulesJSONArray.getJSONObject(i);
			
			String moduleType = jsonInternal.getString("type");
			
			// get the module
			Module moduleCopy = ModuleGenerator.getModule( moduleType );
			
			// System.out.println( "LocalDefEvaluator :: getPreview :: Getting the instance for type : " + moduleType );

			moduleCopy.assignInstanceValues( jsonInternal );
			
			//get the service
			moduleList.add( moduleCopy );
		}
		
		// TODO:use wires for ordering
		// TODO:use modules connectors for input/outputs		
		// TODO: extend checking and ordering logic
		
		// Create a loci workflow from the module instances and wires
		
		
		// Create the workflow from the pipes connection
		//WorkFlow workFlow = LayoutToWorkFlow.getWorkFlow( def.getWires(), moduleList );
		
		
		
		//Create a preview response
		Preview previewResponse = new Preview();
		
		// evaluate the modules individually
		for( Module module : moduleList )
		{
			System.out.println( "LocalDefEvaluator :: getPreview :: Runnning module " + module.getType().getValue()  );	
				
			// module run
			module.go( );
			
			// add the errors
			previewResponse.addErrors( module.getID(), module.getErrors() );
			
			// add the preview
			previewResponse.addPreview( module.getID(), module.getPreviewJSON() );
			
			// add the stats
			previewResponse.addStats( module.getResponse().getTitle(), module.getResponse().getJSON() );
		}
		
		return  previewResponse.getJSON(); 
		
		//return new JSONObject();
	}

}
