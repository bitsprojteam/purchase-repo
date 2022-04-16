package edu.bitspilani.mtech.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private float buyingPrice;
	private String code;
	private String description;
	private int quantity;
	private float sellingPrice;
	private String tranType;
}