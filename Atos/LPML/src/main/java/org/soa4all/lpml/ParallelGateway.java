/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.soa4all.lpml;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IParallelGateway;


/**
 * IParallelGateway is just a marker interface that  
 * specifies parallel split or merge semantics of the execution flow.
 * 
 * The main use case for parallel gateways is to give the execution engine
 * a hint that some unrelated parts of the process can be executed in parallel in order to
 * improve the overall process execution time.
 * Parallelism can increase the performance dramatically in the
 * case of unrelated human tasks or asynchronous web services which are inherently slow.
 * Given N services/human tasks, parallel execution asymptotically yields N times of
 * a performance improvement, because the overall execution time is equal to the time needed
 * to execute the slowest service or human task. 
 * 
 * When a parallel <b>split</b> gateway is reached during process execution, the execution engine
 * may create one parallel execution token (aka thread) for each flow that goes out of this gateway,
 * so that the following activities and human tasks can be executed in parallel. 
 * 
 * A <b>merge</b> gateway may be used to syncronize two or more such tokens back to one and thus seize all
 * or part of the parallel execution. When a <b>merge</b> gateway is reached at runtime, execution shall 
 * wait until all the tokens that could possibly reach the gateway, arrive. This can easily be determined by
 * traversing the process graph backwards in order to find all tokens that could potentially arrive at the
 * gateway.
 * 
 * Since processes may have more than one end element, when a token reaches an end element it is terminated
 * and shall not be waited for at a merge gateway. This makes it possible to terminate all tokens at one 
 * or more end events without the need for a merge gateway. That is, the process terminates when the last
 * token reaches an end element.  
 * 
 */
public interface ParallelGateway extends IParallelGateway{

}
