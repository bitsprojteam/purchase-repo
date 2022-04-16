/**
 * 
 */
package edu.bitspilani.mtech.controller;


import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.bitspilani.mtech.dto.Error;
import edu.bitspilani.mtech.dto.ProductDTO;
import edu.bitspilani.mtech.model.Purchase;
import edu.bitspilani.mtech.model.ResponseModel;
import edu.bitspilani.mtech.repository.PurchaseRepository;

/**
 * @author Mohamed Noohu
 *
 */
@RestController
@RequestMapping("/api")
public class PurchaseController {

private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);
	
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	
	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	PurchaseRepository repo;
	
	@Autowired
    ObjectMapper objectMapper;
	
	@PostMapping("/purchase")
	@Transactional
	public ResponseEntity<?> purchase(@RequestPart String productData) throws JsonMappingException, JsonProcessingException {
		ResponseModel rm = new ResponseModel();
		
		logger.info("Entering purchase....");
		
		Purchase p = this.toPurchase(productData);
		
		if (p.getCode() == null)
			rm.setError("prodCode","Product code must be specified");
		
		if (p.getQuantity() < 0)
			rm.setError("quantity","Quantity must be greater than 0");
		
		if (p.getPrice() < 0)
			rm.setError("price","Price must be greater than 0");
		
		if (!rm.getErrors().isEmpty())
			return ResponseEntity.ok().body(new Error(rm));
		
		String response = restTemplate.getForObject(getProductServiceBaseUri() + "/api/product/code/"+p.getCode(),String.class);
		logger.info("Get response:"+response);
		
		
		if(response.contains("error")) {
			//Error e = this.toError(response);
			//logger.info("Received error response as:"+e.toString());
			return ResponseEntity.ok().body(response);	
		}
		
		ProductDTO pDto = this.toProductDTO(response);
		
		
		if (p.getPrice() < pDto.getSellingPrice())
			rm.setError("price","Purchasing price can't be less than selling price");
		
		if (p.getQuantity() > pDto.getQuantity())
			rm.setError("quantity","Purchasing quantity can't be more than available quantity");
		
		if (!rm.getErrors().isEmpty())
			return ResponseEntity.ok().body(new Error(rm));
		
		p.setDate(new Date());
		p.setTotal(p.getQuantity() * p.getPrice());
		
		repo.save(p);
		
		pDto.setQuantity(p.getQuantity());
		pDto.setTranType("purchase");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.setAccept(Arrays.asList(MediaType.ALL));
		
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("productData", pDto);
        
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		restTemplate.exchange(getProductServiceBaseUri() + "/api/product", HttpMethod.PUT, requestEntity, String.class);
		
		
		logger.info("update response:"+response);
		
		return ResponseEntity.ok().body(null);
		
	}

	private String getProductServiceBaseUri(){
        ServiceInstance serviceInstance =  loadBalancerClient.choose("product-ms");
        return serviceInstance.getUri().toString();
    }
	
	public Purchase toPurchase(String data) throws JsonMappingException, JsonProcessingException {
		return objectMapper.readValue(data, Purchase.class);
	}
	
	public ProductDTO toProductDTO(String data) throws JsonMappingException, JsonProcessingException {
		return objectMapper.readValue(data, ProductDTO.class);
	}
	
	public Error toError(String data) throws JsonMappingException, JsonProcessingException {
		return objectMapper.readValue(data, Error.class);
	}
	
	public String toJsonstring(String data) throws JsonMappingException, JsonProcessingException {
		return objectMapper.writeValueAsString(data);
	}
}
