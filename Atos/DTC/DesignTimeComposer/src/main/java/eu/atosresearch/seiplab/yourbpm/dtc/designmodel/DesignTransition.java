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

//import org.openrdf.sesame.sail.query.CompareAll;

public class DesignTransition implements Comparable<DesignTransition> {
	
	private String SourceURI;
	private String TargetURI;
	
	/**
	 * @return the sourceURI
	 */
	public String getSourceURI() {
		return SourceURI;
	}
	/**
	 * @param sourceURI the sourceURI to set
	 */
	public void setSourceURI(String sourceURI) {
		SourceURI = sourceURI;
	}
	/**
	 * @return the targetURI
	 */
	public String getTargetURI() {
		return TargetURI;
	}
	/**
	 * @param targetURI the targetURI to set
	 */
	public void setTargetURI(String targetURI) {
		TargetURI = targetURI;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SourceURI == null) ? 0 : SourceURI.hashCode());
		result = prime * result + ((TargetURI == null) ? 0 : TargetURI.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DesignTransition other = (DesignTransition) obj;
		if (SourceURI == null) {
			if (other.SourceURI != null)
				return false;
		} else if (!SourceURI.equals(other.SourceURI))
			return false;
		if (TargetURI == null) {
			if (other.TargetURI != null)
				return false;
		} else if (!TargetURI.equals(other.TargetURI))
			return false;
		return true;
	}
	

	@Override
	public int compareTo(DesignTransition o) {
		// TODO Auto-generated method stub
		if (o.hashCode() == this.hashCode()) {
			return 0;
		} else {
			if (o.hashCode() > this.hashCode()) {
				return -1;
			} else {
				return 1;
			}
		}
	}
	
	public String toString() {
		return SourceURI + " > " + TargetURI;
	}
}
