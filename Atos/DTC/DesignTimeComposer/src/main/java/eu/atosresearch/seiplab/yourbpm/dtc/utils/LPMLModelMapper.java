/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.atosresearch.seiplab.yourbpm.dtc.utils;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.soa4all.lpml.impl.ProcessImpl;

/**
 *
 * @author a512471
 */
public class LPMLModelMapper {
    public eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess toProcess(org.soa4all.lpml.Process _process){
    eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess process = new ProcessImpl();
        
    Mapper mapper = new DozerBeanMapper();
    eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess destObject =  mapper.map(_process, eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess.class);
    return destObject;
    }
}
