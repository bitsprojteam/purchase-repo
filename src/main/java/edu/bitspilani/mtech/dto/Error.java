/**
 * 
 */
package edu.bitspilani.mtech.dto;

import java.io.Serializable;

import edu.bitspilani.mtech.model.ResponseModel;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Mohamed Noohu
 *
 */
@NoArgsConstructor
//@AllArgsConstructor
@ToString
public class Error implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ResponseModel error;

	
	/**
	 * @return the error
	 */
	public ResponseModel getError() {
		return error;
	}


	/**
	 * @param error the error to set
	 */
	public void setError(ResponseModel error) {
		this.error = error;
	}


	/**
	 * 
	 */
	public Error(ResponseModel rm) {
		// TODO Auto-generated constructor stub
		
		this.error = rm;
	}


}
