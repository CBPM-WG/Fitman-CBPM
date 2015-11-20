package eu.atosresearch.seiplab.demo;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;


@WebService
public class StockService {
  
   
  @WebResult(name="availabilityResult")
  public int isAvailable(@WebParam(name="idReference") String idReference) {
    System.out.println("idReference request: " + idReference);
   
	if (idReference.equals("0001")){
		System.out.println("isAvailable: 1 ");
		return 1;	
		
	}else{
		System.out.println("isAvailable: 0 ");
		return 0;
	}
  }
  

}
