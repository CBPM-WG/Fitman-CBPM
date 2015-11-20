package eu.atosresearch.seiplab.demo;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;


@WebService
public class ManufacturingService {
  
   
  @WebResult(name="idConfirmManufacturing")
  public String manufacturingOrder(@WebParam(name="idReference") String idReference, @WebParam(name="typeManufact") String typeManufact) {
    System.out.println("Manufacturing request: reference-" + idReference +" type-"+ typeManufact);
   
	if (idReference.equals("0001")){
		System.out.println("idManufacturing: 333 ");
		return "333";	
		
	}else{
		System.out.println("idManufacturing Error: -1 ");
		return "-1";
	}
  }
  

}
