package eu.atosresearch.seiplab.demo;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;



@WebService
public class AddressService {
  
  
  @WebResult(name="address")
  public String findCustomerAddress(@WebParam(name="customerName") String customerName) {
    System.out.println("finding customer by customerName " + customerName);

    if ("kermit".equals(customerName)) {
    	System.out.println("returning customer " + customerName);
    	return "Address of " + customerName;
    } else {
    	throw new RuntimeException("Customer not found!");
    }
  }
  

}
