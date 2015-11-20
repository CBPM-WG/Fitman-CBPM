package eu.atosresearch.seiplab.yourbpm.wsdl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import org.xml.sax.InputSource;
import uk.ac.open.kmi.iserve.commons.io.IOUtil;

/**
 * Hello world!
 *
 */
public class WSDLManager 
{
    private WSDLReader reader;
    private List<Definition> definitionList;

    
    public WSDLManager() throws WSDLException {
        try {
            this.reader = WSDLFactory.newInstance().newWSDLReader();
            definitionList = new ArrayList();		        	   
        } catch (WSDLException ex) {
            Logger.getLogger(WSDLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Definition addWSDL(String uri){
        Definition definition=null;
        try {            
            //File file = new File(uri);
            //String contents = IOUtil.readString(file);
            //InputSource is = new InputSource(new StringReader(contents));                     
            //definition = reader.readWSDL(null, is);
            definition = reader.readWSDL(uri);
            definitionList.add(definition);
            
        } catch (WSDLException ex) {
            Logger.getLogger(WSDLManager.class.getName()).log(Level.SEVERE, null, ex);
        } //catch (IOException ex) {
//            Logger.getLogger(WSDLManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return definition;
    }
    public List<Definition> getDefinitionList() {
        return definitionList;
    }
}
