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
package eu.atosresearch.seiplab.yourbpm.dtc.common.utils.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class ThreadPool {

	private ExecutorService execSvc = Executors.newCachedThreadPool();
	
	private static ThreadPool instance = new ThreadPool();
	
	public static ThreadPool getInstance (){ return instance; }
	
	public void execute (Runnable task){
		try {
			execSvc.execute(task);
		} catch (RejectedExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public void shutdown (){
		execSvc.shutdown();
	}
	
	public void reset(){
		execSvc = Executors.newCachedThreadPool();
	}
}
