package eu.atosresearch.webn1.wsdl.wsdlmanager;

import eu.atosresearch.seiplab.yourbpm.wsdl.WSDLManager;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.wsdl.WSDLException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        try {
            WSDLManager w = new WSDLManager();
            w.addWSDL("src/test/resources/imath.wsdl");
            System.out.print("TargetNamespace: " +w.getDefinitionList().get(0).getTargetNamespace());
        } catch (WSDLException ex) {
            Logger.getLogger(AppTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertTrue( true );
    }
}
