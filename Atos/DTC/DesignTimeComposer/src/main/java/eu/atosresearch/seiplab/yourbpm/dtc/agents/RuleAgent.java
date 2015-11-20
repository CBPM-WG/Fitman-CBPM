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
package eu.atosresearch.seiplab.yourbpm.dtc.agents;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.RuleBaseConfiguration;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.PackageBuilder;
import org.drools.event.DebugAgendaEventListener;
import org.drools.event.DebugWorkingMemoryEventListener;
import org.drools.rule.Package;

import eu.atosresearch.seiplab.yourbpm.dtc.common.namespace.Namespace;
import eu.atosresearch.seiplab.yourbpm.dtc.terminology.Terminology;

public abstract class RuleAgent extends Agent {
	private String ruleBaseFilePath;
	private String domainSpecificLanguage;
	protected RuleBase ruleBase;
	private WorkingMemory workingMemory;
//	private WorkingMemoryFileLogger logger; 
	private List <FactHandle> factList;
	
	// Logger
	static Logger log = Logger.getLogger(RuleAgent.class);

	public RuleAgent(String ruleBaseFilePath, String domainSpecificLanguage) {
		this.ruleBaseFilePath = ruleBaseFilePath;
		this.domainSpecificLanguage = domainSpecificLanguage;
		this.factList = new ArrayList<FactHandle>();
		
		initializeRuleBase();
		executeRules();
	}

	public String getRuleBaseFilePath() {
		return ruleBaseFilePath;
	}

	public void setRuleBaseFilePath(String ruleBaseFilePath) {
		this.ruleBaseFilePath = ruleBaseFilePath;
	}

	public void initializeRuleBase() {
		System.out.println("init rulebase:");
		System.out.println(this.ruleBaseFilePath);

		try {
//			log.debug("Setting DTC class loader: " + DTComposerImpl.getDTCClassLoader()); 
//			Thread.currentThread().setContextClassLoader(DTComposerImpl.getDTCClassLoader());
			
			Reader source = null;
			//Getting rule definition from URL or classpath
			if (ruleBaseFilePath.startsWith("http://") || ruleBaseFilePath.startsWith("file://")){
				URL sourceURL = new URL (ruleBaseFilePath);
				source = new InputStreamReader(sourceURL.openConnection().getInputStream());
			}else{//Default, from classpath
				source = new InputStreamReader(RuleAgent.class
					.getResourceAsStream(ruleBaseFilePath));
			}
			
			Reader dsl = new InputStreamReader( RuleAgent.class.getResourceAsStream( domainSpecificLanguage ) );
			
			PackageBuilder builder = new PackageBuilder();

			builder.addPackageFromDrl(source, dsl);

			Package pkg = builder.getPackage();

			RuleBaseConfiguration conf = new RuleBaseConfiguration();
//			conf.setShadowProxy( false );
			
			this.ruleBase = RuleBaseFactory.newRuleBase(conf);

			this.ruleBase.addPackage(pkg);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not load/compile rules file: "
					+ ruleBaseFilePath, e);
		}

/*
		System.out.println("----->"+ RuleAgent.class
				.getResourceAsStream(this.ruleBaseFilePath));
		
		Reader source = new InputStreamReader(RuleAgent.class
				.getResourceAsStream(this.ruleBaseFilePath));

		PackageBuilder builder = new PackageBuilder();

		try {
			// this wil parse and compile in one step
			// NOTE: There are 2 methods here, the one argument one is for
			// normal DRL.
			builder.addPackageFromDrl(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get the compiled package (which is serializable)
		Package pkg = builder.getPackage();

		// Now we assign the new ruleBase
		this.ruleBase = RuleBaseFactory.newRuleBase();

		try {
			this.ruleBase.addPackage(pkg);
		} catch (Exception e) {

			e.printStackTrace();
		}
*/
	}
	
	public void executeRules() {
		
        this.workingMemory = ruleBase.newStatefulSession();
        
//        final Object a = new Object();
        this.workingMemory.setGlobal("agent", this);

		// setup the debug listeners for debugging purposes
        workingMemory.addEventListener( new DebugAgendaEventListener() );
        workingMemory.addEventListener( new DebugWorkingMemoryEventListener() );
        
        
        // setup the audit for logging (auditview)
//        this.logger = new WorkingMemoryFileLogger( workingMemory );
//        logger.setFileName( "log" + this.getIdentifier() + ruleBase.hashCode());

        // setting namespace
        insertFact(Namespace.DEFAULT);
        
        // setting terminologies
        insertFact (new Terminology());
        
        //callback.initEnvironment(workingMemory);
		workingMemory.fireAllRules();

	}
	
	public void fireRules() {
		this.workingMemory.fireAllRules();
		
		//logger.writeToDisk();
	}
	
//	public void saveLogFile() {
//		logger.writeToDisk();
//	}
	
	public WorkingMemory getWorkingMemory() {
		return workingMemory;
	}

	public void setWorkingMemory(WorkingMemory workingMemory) {
		this.workingMemory = workingMemory;
	}

	public void insertFact(Object fact) {
		FactHandle h = workingMemory.insert(fact);
		factList.add(h);
	}
	
	public void removeFact(Object fact) {
		FactHandle fh = workingMemory.getFactHandle(fact);
		workingMemory.retract(fh);
	}
	
	public void clearWorkingMemory(){
		List <FactHandle> fl = new ArrayList<FactHandle>();
		fl.addAll(factList);
		for (FactHandle h: fl){
			if (h.toExternalForm().contains("eu.soa4all.wp6.composer.agents.events.NewDesignModelEvent")){
				workingMemory.retract(h);
				factList.remove(h);
			}
		}
//		factList = new ArrayList<FactHandle>();
//		((StatefulSession)this.workingMemory).dispose();
//		executeRules();
	}
	
	public void addRulesPackage(Package pack) {
		try {
			ruleBase.addPackage(pack);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
