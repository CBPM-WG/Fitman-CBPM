
package org.example.hotelfind;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.hotelfind package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.hotelfind
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BookRoom }
     * 
     */
    public BookRoom createBookRoom() {
        return new BookRoom();
    }

    /**
     * Create an instance of {@link BookRoomResponse }
     * 
     */
    public BookRoomResponse createBookRoomResponse() {
        return new BookRoomResponse();
    }

}
