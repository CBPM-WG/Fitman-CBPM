/**
 * 
 * Copyright (C) 2006-2010 ATOS ORIGIN
 * http://www.atosorigin.com
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the license LGPL.
 *
 * Authors :
 * Jesus Gorronogoitia. Atos Research and Innovation, Atos Origin SAE
 * @email jesus.gorronogoitia@atosresearch.eu
 * Mateusz Radzimski. Atos Research and Innovation, Atos Origin SAE
 * @email mateusz.radzimski@atosresearch.eu
 */
package eu.atosresearch.seiplab.yourbpm.dtc.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Administrator
 *
 * Temp class
 */
public class FileIOUtils {

	
	public static Reader loadFile(URL url) throws FileNotFoundException {
		Reader freader = new FileReader(url.getFile());
		
		return freader;
		
	}
	

	public static void main(String[] args) throws URISyntaxException, IOException {
		
		
//		String file = "D:/devel/eclipse-workspace/Advance Prototype/DesignTimeComposer_v2/src/main/resources/models/wp9/input/model3.xml";
//		
//		
//		URL url2 = new URL("file:///" + file);
//		System.out.println(url2);
//		
//		BufferedReader input = new BufferedReader(new FileReader(url2.getFile()));
//		
//		System.out.println(input);
//		System.out.println(url2.getPath());
//		
//		URI model3URI = UriBuilder.fromPath(file).scheme("file").build();
//		System.out.println(model3URI);
//		//URI uri = new URI(file);		
//		
//		URL url = model3URI.toURL();
//		
//		String file_good = URLDecoder.decode(url.getPath(), "UTF-8");
//		
//		System.out.println(file_good);
//		//System.out.print();
//		
//		input = new BufferedReader(new FileReader(file_good));
//		System.out.println(input);
	
	}
}
