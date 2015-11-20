package org.soa4all.lpml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.junit.Ignore;

@Ignore
public class TestUtil {
	
	@SuppressWarnings("unchecked")
	public static <T> T deepCopy(T source){

		ObjectInputStream in = null;
		try {

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            
            out.writeObject(source);
            out.flush();
            out.close();

            in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            return (T) in.readObject();
        } catch(Exception e) {
        	throw new RuntimeException(e);
        } finally {
        	if (in != null){
        		try {
        			in.close();
        		} catch (Throwable t){
        			System.err.println("An error occured while trying closing the input stream");
        			t.printStackTrace();
        		}
        	}
        }
	}

	private static boolean atSAP(){
		return "SAP_ALL".equals(System.getenv("USERDOMAIN"));
	}
	
	public static void setProxy(HttpClient client){
		if(atSAP()){
			client.getHostConfiguration().setProxy("proxy", 8080);	
		}
	}
}
