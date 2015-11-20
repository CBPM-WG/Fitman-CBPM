/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.soa4all.lpml.impl;


import java.io.Serializable;

import org.soa4all.lpml.ParallelGateway;

public class ParallelGatewayImpl extends ProcessElementImpl implements ParallelGateway,Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private boolean isSplit;
	private float x = Float.NaN, y = Float.NaN, z = Float.NaN;
	
	public ParallelGatewayImpl(boolean isSplit) {
		super();
		this.isSplit = isSplit;
	}
	
	public ParallelGatewayImpl(boolean isSplit, String id) {
		super(id);
		this.isSplit = isSplit;
	}

	@Override
	public boolean isSplit() {
		return isSplit;
	}
	
	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getZ() {
		return z;
	}

	@Override
	public void setX(float position) {
		this.x = position;
		
	}

	@Override
	public void setY(float position) {
		this.y = position;
		
	}

	@Override
	public void setZ(float position) {
		this.z = position;
		
	}
	
	

	@Override
	public int hashCode() {
		return getID().hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ParallelGatewayImpl other = (ParallelGatewayImpl) obj;
		
		if(this.isSplit != other.isSplit){
			return false;
		}
		
		return true;
	}

	@Override
	public String toString() {
		
		StringBuffer result = new StringBuffer(super.toString());
		result.append(this.getClass().getName());
		result.append(", id: ");
		result.append(getID());
		result.append(", isSplit: ");
		result.append(isSplit);
		return result.toString();
	}
}
