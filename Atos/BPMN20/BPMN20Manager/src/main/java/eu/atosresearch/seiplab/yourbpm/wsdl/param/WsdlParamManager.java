package eu.atosresearch.seiplab.yourbpm.wsdl.param;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ow2.easywsdl.schema.api.ComplexType;
import org.ow2.easywsdl.schema.api.Element;
import org.ow2.easywsdl.schema.api.Schema;
import org.ow2.easywsdl.schema.api.Sequence;
import org.ow2.easywsdl.schema.api.Type;
import org.ow2.easywsdl.wsdl.WSDLFactory;
import org.ow2.easywsdl.wsdl.api.Description;
import org.ow2.easywsdl.wsdl.api.Types;

import org.ow2.easywsdl.wsdl.api.WSDLReader;

/**
 * @author S270094
 * This class provides the parameters Input/output associates to a WSDL
 */
public class WsdlParamManager {
	
	private WSDLReader reader;
	private Description desc;
	
	public WsdlParamManager(String wsdlUrl) throws Exception{
		try {
			reader = WSDLFactory.newInstance().newWSDLReader();

				desc = reader.read(new URL(wsdlUrl));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**It recovers all the parameters associated to complexType (identify by the name):
	 * <wsdl:types>
	 * <xsd:element name="SearchFligths" type="tns:Flights"/>
	 * <xsd:complexType name="Flights">
	 * 	<xsd:sequence>
	 * 		<xsd:element name="Departure" type="xsd:string"/>
	 * 		<xsd:element name="Destination" type="xsd:string"/>
	 * 		<xsd:element name="DepartureDate" type="xsd:string"/>
	 * 		<xsd:element name="ReturnDate" type="xsd:string"/>
	 * 	</xsd:sequence>
	 * </xsd:complexType>
	 * </wsdl:types>
	 *The referenceName: SearchFligths returns {[Departure, string], [Departure, string], [Destination, string], [DepartureDate, string], [ReturnDate, string]}
	 * @param referenceName Name of the reference of type (included in the WSDL) that we want to recover
	 * @return
	 */
	public List<WsdlParameter> getWsdlParameters(String referenceName){
		
		List<WsdlParameter> listParameters = new ArrayList<WsdlParameter>();
		//recover the types of the wsdl
		Types t = desc.getTypes();
		List<Schema> listSchema = t.getSchemas();
		for (Iterator iterator = listSchema.iterator(); iterator.hasNext();) {
			Schema schema = (Schema) iterator.next();
			List<Type> typeList = schema.getTypes();
			for (Iterator iterator2 = typeList.iterator(); iterator2
					.hasNext();) {
				Type type = (Type) iterator2.next();
				String nameType = type.getQName().getLocalPart();
				
				if (referenceName.equals(nameType)) {
					//recover the associated elements
					ComplexType complexType = (ComplexType) type;
					Sequence sequence = complexType.getSequence();
					List<Element> elements = sequence.getElements();
					for (Iterator iterator3 = elements.iterator(); iterator3
							.hasNext();) {
						Element element = (Element) iterator3.next();
						String nameParam = element.getQName().getLocalPart();
						String typeParam = element.getType().getQName().getLocalPart();
						WsdlParameter wsdlParam = new WsdlParameter();
						wsdlParam.setName(nameParam);
						wsdlParam.setTypes(typeParam);
						listParameters.add(wsdlParam);
					}
					
				}
			}
		}
		return listParameters;
		
	}
	

}
