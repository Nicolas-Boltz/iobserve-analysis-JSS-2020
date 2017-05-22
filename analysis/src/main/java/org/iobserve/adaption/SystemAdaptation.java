package org.iobserve.adaption;

import org.iobserve.adaption.data.AdaptationData;
import org.iobserve.analysis.utils.AbstractLinearComposition;

/**
 * This class encapsulates the system adaption filter in the teetime framework.
 * It contains the sub-stages: compare/action generation, execution planning and
 * actual execution.
 * 
 * @author Philipp Weimann
 */
public class SystemAdaptation extends AbstractLinearComposition<AdaptationData, AdaptationData> {

	/**
	 * This class encapsulates the major system adaption filter stage.
	 * 
	 * @param comparer
	 *            computes action which need to be done for migration/adaption
	 * @param planner
	 *            orders the actions into a executable sequence
	 * @param executer
	 *            executes the ordered actins
	 */
	public SystemAdaptation(AdaptationCalculation comparer, AdaptationPlanning planner, AdaptationExecution executer) {
		super(comparer.getInputPort(), executer.getOutputPort());

		this.connectPorts(comparer.getOutputPort(), planner.getInputPort());
		this.connectPorts(planner.getOutputPort(), executer.getInputPort());
	}

}
