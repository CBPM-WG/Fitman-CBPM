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
package eu.atosresearch.seiplab.yourbpm.dtc.designmodel;

import java.util.ArrayList;
import java.util.List;

public class DesignModelStatus {
	private List<DesignModelStatusEnum> statuslist = new ArrayList();
	
	public void changeStatus (DesignModelStatusEnum status){
		removeOppositeStatus (status);
		if (!statuslist.contains(status)){
			this.statuslist.add(status);
		}
	}
	
	private void removeOppositeStatus (DesignModelStatusEnum status){
		switch (status){
			case ADMISSIBLE:
				statuslist.remove(DesignModelStatusEnum.INADMISSIBLE);
				statuslist.remove(DesignModelStatusEnum.NOT_CHECKED);
			break;
			case INADMISSIBLE:
				statuslist.remove(DesignModelStatusEnum.ADMISSIBLE);
				statuslist.remove(DesignModelStatusEnum.NOT_CHECKED);
			break;
			case SUITABLE:
				statuslist.remove(DesignModelStatusEnum.NOT_SUITABLE);
				statuslist.remove(DesignModelStatusEnum.NOT_CHECKED);
			break;
			case NOT_SUITABLE:
				statuslist.remove(DesignModelStatusEnum.SUITABLE);
				statuslist.remove(DesignModelStatusEnum.NOT_CHECKED);
			break;
			case COMPLETE:
				statuslist.remove(DesignModelStatusEnum.INCOMPLETE);
				statuslist.remove(DesignModelStatusEnum.NOT_CHECKED);
			break;
			case INCOMPLETE:
				statuslist.remove(DesignModelStatusEnum.COMPLETE);
				statuslist.remove(DesignModelStatusEnum.NOT_CHECKED);
			break;
			case IO_COMPATIBLE:
				statuslist.remove(DesignModelStatusEnum.IO_INCOMPATIBLE);
				statuslist.remove(DesignModelStatusEnum.IO_UNCHECKED_SOLUTION);
			break;
			case IO_INCOMPATIBLE:
				statuslist.remove(DesignModelStatusEnum.IO_COMPATIBLE);
				statuslist.remove(DesignModelStatusEnum.IO_UNCHECKED_SOLUTION);
			break;
		}
	}
	
	public boolean isSolution(){
		boolean result = false;
		result = statuslist.contains(DesignModelStatusEnum.ADMISSIBLE) &&
			statuslist.contains(DesignModelStatusEnum.SUITABLE) &&
			statuslist.contains(DesignModelStatusEnum.COMPLETE) &&
			statuslist.contains(DesignModelStatusEnum.IO_COMPATIBLE);
		return result;
	}
	
	public boolean isIOUncheckedSolution(){
		boolean result = false;
		result = statuslist.contains(DesignModelStatusEnum.ADMISSIBLE) &&
			statuslist.contains(DesignModelStatusEnum.SUITABLE) &&
			statuslist.contains(DesignModelStatusEnum.COMPLETE) &&
			statuslist.contains(DesignModelStatusEnum.IO_UNCHECKED_SOLUTION);
		return result;
	}
	
	public boolean isIncompleteSolution(){
		boolean result = false;
		result = statuslist.contains(DesignModelStatusEnum.ADMISSIBLE) &&
			(statuslist.contains(DesignModelStatusEnum.SUITABLE) || statuslist.contains(DesignModelStatusEnum.NOT_SUITABLE)) &&
			statuslist.contains(DesignModelStatusEnum.INCOMPLETE);
		return result;
	}
	
	public boolean isComplete(){
		return statuslist.contains(DesignModelStatusEnum.COMPLETE);
	}
	
	public boolean isInadmissible(){
		return statuslist.contains(DesignModelStatusEnum.INADMISSIBLE);
	}
	
	public boolean isAdmissible(){
		return statuslist.contains(DesignModelStatusEnum.ADMISSIBLE);
	}
	
	public boolean isNotSuitable() {
		return statuslist.contains(DesignModelStatusEnum.NOT_SUITABLE);
	}

	public boolean isSuitable() {
		return statuslist.contains(DesignModelStatusEnum.SUITABLE);
	}

	public boolean isNotChecked() {
		return statuslist.contains(DesignModelStatusEnum.NOT_CHECKED);
	}
	
	public boolean isIOCompatible() {
		return statuslist.contains(DesignModelStatusEnum.IO_COMPATIBLE);
	}
	
	public boolean isIOIncompatible() {
		return statuslist.contains(DesignModelStatusEnum.IO_INCOMPATIBLE);
	}
	
	public boolean isNoSolution() {
		return (isInadmissible() || isIOIncompatible())
			|| (isComplete() && isNotSuitable());
	}
	
	public String toString (){
		StringBuffer sb = new StringBuffer();
		sb.append(isNotChecked()?"UNCHECKED":"");
		sb.append(isSolution()?"SOLUTION":"");
		sb.append(isIOUncheckedSolution()?"IOUNCHECKED SOLUTION":"");
		sb.append(isIncompleteSolution()?"INCOMPLETE SOLUTION":"");
		if (isNoSolution()){
			sb.append("NO SOLUTION");
		}
		sb.append("[");
		for (DesignModelStatusEnum status: statuslist){
			sb.append(status + ",");
		}
		sb.append("]");
		return sb.toString();
	}

	public boolean isSet() {
		return !statuslist.isEmpty();
	}

	public void emptyStatus() {
		statuslist = new ArrayList();
	}

	
}
