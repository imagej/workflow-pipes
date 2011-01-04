package pipesentity;

import java.util.ArrayList;

import pipesentity.TerminalConnectorType.inputType;
import pipesentity.TerminalConnectorType.outputType;

public class Terminal {
	
	//populate with defaults
	private String typeKey;
	private String typeValue;
	private String directionKey = "name";
	private String directionValue;

	/**
	 * 
	 * @param input If the terminal is of Input Type
	 * @param terminalConnectorType - Enumerated values of supported types
	 */
	public Terminal( boolean isInputTypeOfTerminal, String terminalConnectorType )
	{
		if (isInputTypeOfTerminal)
		{
			directionValue = "_INPUT";
			this.typeKey = "input";
		}
		else //output
		{
			directionValue = "_OUTPUT";
			this.typeKey = "output";
		}
		
		//assign type value
		this.typeValue = terminalConnectorType;
	}
	
	public String getTypeKey() {
		return this.typeKey;
	}
	
	public String getTypeValue() {
		return this.typeValue;
	}

	public String getDirectionKey() {
		return directionKey;
	}

	public String getDirectionKeyValue() {
		return directionValue;
	}

	/**
	 * @return
	 */
	public static ArrayList<Terminal> getInOutTerminal( inputType inputType1, outputType outputType1 ) 
	{
		ArrayList<Terminal> terminals = new ArrayList<Terminal>();
		terminals.add( new Terminal( true, inputType1.toString()  ) );
		terminals.add(  new Terminal( false, outputType1.toString() ) );
 		return terminals;
	}

	public static ArrayList<Terminal> getInputTerminal( inputType inputType1 ) {
		ArrayList<Terminal> terminals = new ArrayList<Terminal>();
		terminals.add( new Terminal( true, inputType1.toString()  ) );
 		return terminals;
	}

	public static ArrayList<Terminal> getOutTerminal( outputType outputType1 ) {
		ArrayList<Terminal> terminals = new ArrayList<Terminal>();
		terminals.add( new Terminal( false, outputType1.toString() )  );
 		return terminals;
	}

	public static ArrayList<Terminal> getTerminals( String string ) {
		ArrayList<Terminal> terminals = new ArrayList<Terminal>();
		
		
		return terminals;
	}
	
	

}
