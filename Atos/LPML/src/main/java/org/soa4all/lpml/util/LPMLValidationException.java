package org.soa4all.lpml.util;

public class LPMLValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public LPMLValidationException(String message) {
		super(message);
	}

	public LPMLValidationException(Throwable e) {
		super(e);
	}
	
	public LPMLValidationException(String message, Throwable e) {
		super(message, e);
	}

	

}
