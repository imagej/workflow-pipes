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

package imagej.workflowpipes.pipesentity;

import java.io.Serializable;
import java.lang.annotation.Target;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents the JSON wire entity
 * @author rick
 *
 */
public class Wire implements Serializable {
	
	private ID id;
	private Src src;
	private Tgt tgt;
	
	public Wire( ID id, Src src, Tgt tgt )
	{
		this.id = id;
		this.src = src;
		this.tgt = tgt;
	}
	
	public Wire( JSONObject jsonObject ) 
	{
		//System.out.println("Wire Constructor input " + jsonObject );
		this.id = new ID( jsonObject.getString("id") );
		this.src = new Src( jsonObject.getJSONObject("src") );
		this.tgt = new Tgt( jsonObject.getJSONObject("tgt") );
		//System.out.println("Wire Constructor output " + getJSONObject() );	
	}

	public JSONObject getJSONObject()
	{
		JSONObject json = new JSONObject();
		
		//populate the json object
		json.put( "id", id.getValue() );
		json.put( "src", src.getJSONObject() );
		json.put( "tgt", tgt.getJSONObject() );
		
		return json;
	}

	public static ArrayList<Wire> getArrayFromJSONArray( JSONArray wiresArray ) 
	{
		ArrayList<Wire> wires = new ArrayList<Wire>();
		
		//for each wire
		for(int i = 0; i < wiresArray.length(); i++)
			wires.add( new Wire( wiresArray.getJSONObject(i) ) );
		return wires;
	}

	public Src getSrc() {
		return this.src;
	}

	public Tgt getTgt() {
		return this.tgt;
	}

}
