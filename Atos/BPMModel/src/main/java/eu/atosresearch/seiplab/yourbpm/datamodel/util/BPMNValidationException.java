package eu.atosresearch.seiplab.yourbpm.datamodel.util;

public class BPMNValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public BPMNValidationException(String message) {
		super(message);
	}

	public BPMNValidationException(Throwable e) {
		super(e);
	}
	
	public BPMNValidationException(String message, Throwable e) {
		super(message, e);
	}

	

}
