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
package eu.atosresearch.seiplab.yourbpm.dtc.viewer;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

import prefuse.controls.ControlAdapter;
import prefuse.visual.VisualItem;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModel;
import eu.atosresearch.seiplab.yourbpm.dtc.utils.ModelIOUtils;

public class ClickControlAdapter extends ControlAdapter {

	@Override
	public void itemClicked(VisualItem item, MouseEvent e) {
		try {
			DesignModel designModel = (DesignModel)item.get("designmodel");
			
			System.out.println(designModel.getTransitionChain().toString());
			
			URL outputURL =  ModelIOUtils.saveTemporalModel(designModel);
			String outputPath = outputURL.getFile();
			String os = System.getProperty("os.name", "not specified");
			System.out.println("OS: " + os);
			String cmd = null;
			if (os.indexOf("Windows") != -1){
				//in windows we cant allow the trailing backslash in the file name.
				cmd = "cmd /c start scripts\\run.bat \"" + outputPath.substring(1) + "\"";
			} else if (os.indexOf("Linux") != -1){
				cmd = "./scripts/run.sh "  + outputPath;
			}
			System.out.println(cmd);
			System.out.println(System.getProperty("user.dir"));
			Runtime.getRuntime().exec(cmd);
		} catch (Exception e1) {
			throw new RuntimeException("External command exception",e1);
		}
	}
}
