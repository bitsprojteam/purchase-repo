
   
/**
 * 
 */
package edu.bitspilani.mtech.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author Mohamed Noohu
 *
 */
@JsonInclude(Include.NON_NULL)
public class ResponseModel {

	@JsonIgnore
	private Map<String, String> errors = new HashMap<>();
	
	
	
	//map -> map.entrySet().stream().map(Entry::toString).collect(joining(";", "[", "]"))
	/**
	 * @return the errors
	 */
	public Map<String, String> getErrors() {
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	public void setError(String field, String error) {
		this.errors.put(field, error);
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	@JsonValue
	@JsonRawValue
	public String toString() {
		String payload = null;
		try {
			payload = new ObjectMapper().writeValueAsString(this.errors);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return payload;
	}
}