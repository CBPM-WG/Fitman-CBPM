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
package eu.atosresearch.seiplab.yourbpm.dtc.run;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Scanner;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.soa4all.lpml.Process;

import eu.atosresearch.seiplab.yourbpm.dtc.DTComposerImpl;
import eu.atosresearch.seiplab.yourbpm.dtc.common.namespace.Namespace;
import eu.atosresearch.seiplab.yourbpm.dtc.common.utils.rdf.LPMLXMLMapper;
import eu.atosresearch.seiplab.yourbpm.dtc.exception.DTComposerException;


public class DesignComposerDemonstration {
	public static Namespace NAMESPACE = Namespace.WP9_NAMESPACE;
	public static String INPUT_MODELS_DIR;
	public static String INPUT_MODELS_URL;
	public static String OUTPUT_MODELS_DIR;
	public static String OUTPUT_MODELS_URL;
	public static String TEMPLATES_DIR;
	public static String TEMPLATES_URL;
	public static String TEMP_DIR;
	public static String TEMP_URL;

	DTComposerImpl composerApplication;

	public DesignComposerDemonstration(String springConfiguration)
			throws URISyntaxException, DTComposerException {
		composerApplication = new DTComposerImpl();
		composerApplication.init(springConfiguration);

		DesignComposerDemonstration.INPUT_MODELS_DIR = DTComposerImpl
				.getInstance().getConfiguration().getINPUT_MODELS_DIR();
		DesignComposerDemonstration.INPUT_MODELS_URL = DTComposerImpl
				.getInstance().getConfiguration().getINPUT_MODELS_URL();
		DesignComposerDemonstration.OUTPUT_MODELS_DIR = DTComposerImpl
				.getInstance().getConfiguration().getOUTPUT_MODELS_DIR();
		DesignComposerDemonstration.OUTPUT_MODELS_URL = DTComposerImpl
				.getInstance().getConfiguration().getOUTPUT_MODELS_URL();
		DesignComposerDemonstration.TEMPLATES_DIR = DTComposerImpl
				.getInstance().getConfiguration().getTEMPLATES_DIR();
		DesignComposerDemonstration.TEMPLATES_URL = DTComposerImpl
				.getInstance().getConfiguration().getTEMP_URL();
		DesignComposerDemonstration.TEMP_DIR = System.getProperty("user.dir")
				+ "/tmp/";
		DesignComposerDemonstration.TEMP_URL = "file:///"
				+ System.getProperty("user.dir") + "/tmp/";
	}

//	protected ModelSolution[] doResolveProcessWithTemplate(
//			String sourceProcessModelURI, int numberRequestedSolutions) {
//		System.out.println("Executing DesignTimeComposer with input model: "
//				+ sourceProcessModelURI);
//		ModelSolution[] solutions = null;
//
//		try {
//			solutions = composerApplication.resolveProcessWithTemplateMS(
//					sourceProcessModelURI, numberRequestedSolutions);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("DesignTimeComposer has computed final model!");
//		return solutions;
//	}

//	protected ModelSolution[] doResolveProcessWithWS(
//			String sourceProcessModelURI, int numberRequestedSolutions) {
//		System.out.println("Executing DesignTimeComposer with input model: "
//				+ sourceProcessModelURI);
//		ModelSolution[] solutions = null;
//
//		try {
//			solutions = composerApplication.resolveProcessWithWSMS(
//					sourceProcessModelURI, numberRequestedSolutions);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("DesignTimeComposer has computed final model!");
//		return solutions;
//	}

//	protected ModelSolution[] doResolveGoal(String sourceProcessModelURI,
//			String goalTargetURI, int numberRequestedSolutions) {
//		System.out.println("Executing DesignTimeComposer with input model: "
//				+ sourceProcessModelURI);
//		ModelSolution[] solutions = null;
//
//		try {
//			solutions = composerApplication.resolveGoalMS(
//					sourceProcessModelURI, goalTargetURI,
//					numberRequestedSolutions);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("DesignTimeComposer has computed final model!");
//		return solutions;
//	}

//	protected ModelSolution[] doResolveGoalWithTemplate(
//			String sourceProcessModelURI, String goalTarget,
//			int numberRequestedSolutions) {
//		System.out.println("Executing DesignTimeComposer with input model: "
//				+ sourceProcessModelURI);
//		ModelSolution[] solutions = null;
//
//		try {
//			solutions = composerApplication
//					.resolveGoalWithTemplateMS(sourceProcessModelURI,
//							goalTarget, numberRequestedSolutions);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("DesignTimeComposer has computed final model!");
//		return solutions;
//	}

//	protected ModelSolution[] doResolveGoalWithWS(String sourceProcessModelURI,
//			String goalTargetURI, int numberRequestedSolutions) {
//		System.out.println("Executing DesignTimeComposer with input model: "
//				+ sourceProcessModelURI);
//		ModelSolution[] solutions = null;
//
//		try {
//			solutions = composerApplication.resolveGoalWithWSMS(
//					sourceProcessModelURI, goalTargetURI,
//					numberRequestedSolutions);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("DesignTimeComposer has computed final model!");
//		return solutions;
//	}

	public void endDemo() throws MalformedURLException {
		composerApplication.exit();
	}

	public void showModel(URI uri) throws IOException {
		URL location = uri.toURL();
		String filename = location.getFile();

		if (location.getProtocol().equalsIgnoreCase("http")) {
			filename = copyRemoteModel(location);
		}

		String os = System.getProperty("os.name", "not specified");
		String cmd = null;
		if (os.indexOf("Windows") != -1) {
			// in windows we cant allow the trailing backslash in the file name.
			cmd = "cmd /c start scripts\\run.bat \""
					+ URLDecoder.decode(filename, "UTF-8").substring(1) + "\"";
		} else if (os.indexOf("Linux") != -1) {
			cmd = "./scripts/run.sh " + URLDecoder.decode(filename, "UTF-8");
		}
		System.out.println("invoking cmd: " + cmd);
		Runtime.getRuntime().exec(cmd);
		pressAnyKey();
	}

	public void showModel(URL location) throws IOException {
		String filename = location.getFile();

		if (location.getProtocol().equalsIgnoreCase("http")) {
			filename = copyRemoteModel(location);
		}

		String os = System.getProperty("os.name", "not specified");
		String cmd = null;
		if (os.indexOf("Windows") != -1) {
			// in windows we cant allow the trailing backslash in the file name.
			cmd = "cmd /c start scripts\\run.bat \""
					+ URLDecoder.decode(filename, "UTF-8").substring(1) + "\"";
		} else if (os.indexOf("Linux") != -1) {
			cmd = "./scripts/run.sh " + URLDecoder.decode(filename, "UTF-8");
		}
		System.out.println("invoking cmd: " + cmd);
		Runtime.getRuntime().exec(cmd);
//		pressAnyKey();
	}

	public String copyRemoteModel(URL location) throws IOException,
			FileNotFoundException {
		// Get model from server and store it within the local filesystem
		URLConnection connection = location.openConnection();
		BufferedInputStream in = new BufferedInputStream(connection
				.getInputStream());
		String filename = location.getPath().substring(
				location.getPath().lastIndexOf('/') + 1);
		File outfile = new File(DesignComposerDemonstration.TEMP_DIR + filename);
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(outfile));

		StringBuffer sb = new StringBuffer();
		int byte_, counter = 0;
		while ((byte_ = in.read()) != -1) {
			out.write(byte_);
			counter++;
		}
		in.close();
		out.flush();
		out.close();

		System.out.println("Remote model length: "
				+ connection.getContentLength());
		System.out.println("Saved model length: " + counter);

		return outfile.getCanonicalPath();
	}

	public void showModelForTest(URI uri) throws IOException,
			URISyntaxException {
		URL location = uri.toURL();
		String filename = location.getFile();
		if (location.getProtocol().equalsIgnoreCase("http")) {
			filename = copyRemoteModel(location);
		}
		String os = System.getProperty("os.name", "not specified");
		String cmd = null;
		String script;
		if (os.indexOf("Windows") != -1) {
			// in windows we cant allow the trailing backslash in the file name.
			script = "..\\scripts\\run.bat";
			cmd = "cmd /c start " + script + " \""
					+ URLDecoder.decode(filename, "UTF-8").substring(1) + "\"";
		} else if (os.indexOf("Linux") != -1) {
			script = "../scripts/run-test.sh";
			cmd = script + " " + URLDecoder.decode(filename, "UTF-8");
		}
		System.out.println("invoking cmd: " + cmd);
		Runtime.getRuntime().exec(cmd);
	}

	public void pressAnyKey() {
//		if (!getComposer().getConfiguration().isDebug()) {
//			return;
//		}

		Scanner keyIn = new Scanner(System.in);
		System.out.print("Press the enter key to continue");
		keyIn.nextLine();
	}

	public DTComposerImpl getComposer() {
		return composerApplication;
	}

	private Process loadModelFromStorage(String templateName)
			throws URISyntaxException, IOException {

		Process process = null;
		String storate_url = "http://coconut.tie.nl:8080/storage";
		String process_repository = "composer-models";

		String processAsString = getFile(process_repository, templateName,
				storate_url);
		LPMLXMLMapper xmlMapper = new LPMLXMLMapper();
		process = xmlMapper.mapFromXML(processAsString);

		// Bug fixed: replace Identifiers in Process Element to ensure a unique
		// template
//		LPMLProxy.renewIdentifiers(process);

		return process;
	}

	public String loadModelFromStorageAsXML(String templateName)
			throws URISyntaxException, IOException {

		String storate_url = "http://coconut.tie.nl:8080/storage";
		String process_repository = "composer-models";

		String processAsString = getFile(process_repository, templateName,
				storate_url);

		return processAsString;
	}

	public String loadTemplateFromStorageAsXML(String templateName)
			throws URISyntaxException, IOException {

		String storate_url = "http://coconut.tie.nl:8080/storage";
		String process_repository = "composer-templates";

		String processAsString = getFile(process_repository, templateName,
				storate_url);

		return processAsString;
	}

	public String getFile(String repoID, String fileName,
			String STORAGE_SERVICE_ENDPOINT) throws IOException {

		String requestURL = STORAGE_SERVICE_ENDPOINT + "/repositories/"
				+ repoID + "/files/" + fileName;

		System.out.println("Request URL: " + requestURL);
		GetMethod getMtd = new GetMethod(requestURL);
		HttpClient httpclient = new HttpClient();
		String fileDataAsString = null;

		try {
			int result = httpclient.executeMethod(getMtd);
			System.out.println("Response status code: " + result);
			if (result != 200) {
				System.out.println("Error message: ");
				System.out.println(getMtd.getResponseHeader("Error"));
			} else {
				System.out.println("Response OK");
				System.out.println(getMtd.getResponseHeader("Content-Type"));
				fileDataAsString = getMtd.getResponseBodyAsString();
				// System.out.println((fileDataAsString.length() <= 500) ?
				// fileDataAsString
				// : fileDataAsString.substring(0, 500) + " ... ");
			}
		} finally {
			getMtd.releaseConnection();
		}

		return fileDataAsString;
	}

}
