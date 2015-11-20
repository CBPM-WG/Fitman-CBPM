package eu.atosresearch.seiplab.yourbpm.dtc.client;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;



public class BindingCompatibilityDescriptorImpl implements  BindingCompatibilityDescriptor{

	private IBinding binding;
	private String message;
	private float compatibility;
	
	public BindingCompatibilityDescriptorImpl (IBinding binding, String message, float compatibility){
		this.binding = binding;
		this.message = message;
		this.compatibility = compatibility;
	}
	
	public IBinding getBinding() {
		return binding;
	}

	public String getMessage() {
		return message;
	}

	public float getCompatibility() {
		return compatibility;
	}
	
	public String toString() {
		String s = "Binding " + binding.getName() + "(" + binding.getServiceReference() + ")" + " has compatibility " + compatibility + ". Message: " + message;
		return s;
	}

}
