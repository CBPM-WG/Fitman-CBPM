package eu.atosresearch.seiplab.yourbpm.dtc.model;

import eu.atosresearch.seiplab.yourbpm.dtc.exception.UnsupportedModelTypeException;
import eu.atosresearch.seiplab.yourbpm.dtc.model.bpmn.BPMNProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.model.lpml.LPMLProxy;

public class ModelFactory {
	public static enum MODELTYPE {LPML_MODEL, BPMN_MODEL};
	public static ModelProxy getModelProxy (MODELTYPE type){
		ModelProxy proxy = null;
		switch (type){
			case BPMN_MODEL:
				proxy = new BPMNProxy();
				break;
			case LPML_MODEL:
				proxy = new LPMLProxy();
				break;
			default:
			try {
				throw new UnsupportedModelTypeException();
			} catch (UnsupportedModelTypeException e) {
				e.printStackTrace();
			}
		}
		return proxy;
	}
	
}
