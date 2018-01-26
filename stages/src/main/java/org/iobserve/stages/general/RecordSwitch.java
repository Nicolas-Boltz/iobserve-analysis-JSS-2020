/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.stages.general;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.misc.KiekerMetadataRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.iobserve.common.record.IAllocationEvent;
import org.iobserve.common.record.IDeallocationEvent;
import org.iobserve.common.record.IDeployedEvent;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.common.record.IUndeployedEvent;

/**
 * The record switch filter is used to scan the event stream and send events based on their type to
 * different output ports.
 *
 * @author Reiner Jung
 * @author Christoph Dornieden
 *
 */
public class RecordSwitch extends AbstractConsumerStage<IMonitoringRecord> {

    private static final Logger LOGGER = LogManager.getLogger(RecordSwitch.class);

    private static final int LOOP_COUNT = 1000;

    /** output port for deployment events. */
    private final OutputPort<IDeployedEvent> deployedOutputPort = this.createOutputPort();
    /** output port for undeployment events. */
    private final OutputPort<IUndeployedEvent> undeployedOutputPort = this.createOutputPort();
    /** output port for allocation events. */
    private final OutputPort<IAllocationEvent> allocationOutputPort = this.createOutputPort();
    /** output port for deallocation events. */
    private final OutputPort<IDeallocationEvent> deallocationOutputPort = this.createOutputPort();

    /** output port for flow events. */
    private final OutputPort<IFlowRecord> flowOutputPort = this.createOutputPort();
    /** output port for {@link TraceMetadata}. */
    private final OutputPort<TraceMetadata> traceMetadataOutputPort = this.createOutputPort();

    private final OutputPort<ISessionEvent> sessionEventOutputPort = this.createOutputPort();

    /** internal map to collect unknown record types. */
    private final Map<String, Integer> unknownRecords = new ConcurrentHashMap<>();

    /** Statistics. */
    private int recordCount;

    /**
     * Empty default constructor.
     */
    public RecordSwitch() {
        // nothing to do here
    }

    @Override
    protected void execute(final IMonitoringRecord element) {
        this.recordCount++;
        if (this.recordCount % RecordSwitch.LOOP_COUNT == 0) {
            RecordSwitch.LOGGER.debug("Records processed " + this.recordCount);
        }
        if (element instanceof IDeployedEvent) {
            this.deployedOutputPort.send((IDeployedEvent) element);
        } else if (element instanceof ISessionEvent) {
            this.sessionEventOutputPort.send((ISessionEvent) element);
        } else if (element instanceof IUndeployedEvent) {
            this.undeployedOutputPort.send((IUndeployedEvent) element);
        } else if (element instanceof IAllocationEvent) {
            this.allocationOutputPort.send((IAllocationEvent) element);
        } else if (element instanceof IDeallocationEvent) {
            this.deallocationOutputPort.send((IDeallocationEvent) element);
        } else if (element instanceof IFlowRecord) {
            this.flowOutputPort.send((IFlowRecord) element);
            if (element instanceof TraceMetadata) {
                this.traceMetadataOutputPort.send((TraceMetadata) element);
            }
        } else if (element instanceof KiekerMetadataRecord) {
            final KiekerMetadataRecord metadata = (KiekerMetadataRecord) element;
            RecordSwitch.LOGGER.info("Kieker Metadata\n" + "\ncontroller name   " + metadata.getControllerName()
                    + "\nexperiment id     " + metadata.getExperimentId() + "\nhostname          "
                    + metadata.getHostname() + "\nlogging timestamp " + metadata.getLoggingTimestamp()
                    + "\nnumber of records " + metadata.getNumberOfRecords() + "\nsize              "
                    + metadata.getSize() + "\ntime offset       " + metadata.getTimeOffset() + "\nunit              "
                    + metadata.getTimeUnit() + "\nversion           " + metadata.getVersion());
        } else {
            final String className = element.getClass().getCanonicalName();
            Integer hits = this.unknownRecords.get(className);
            if (hits == null) {
                RecordSwitch.LOGGER.error("Configuration error: New unknown event type " + className);
                this.unknownRecords.put(className, Integer.valueOf(1));
            } else {
                hits++;
                this.unknownRecords.put(className, hits);
                if (hits % RecordSwitch.LOOP_COUNT == 0) {
                    RecordSwitch.LOGGER.error("Event occurances " + hits + " of unknown eventtype " + className);
                }
            }
        }
    }

    @Override
    public void onTerminating() {
        RecordSwitch.LOGGER.info("Records processed in total " + this.recordCount);
        super.onTerminating();
    }

    /**
     * @return the deploymentOutputPort
     */
    public final OutputPort<IDeployedEvent> getDeployedOutputPort() {
        return this.deployedOutputPort;
    }

    /**
     * @return the undeploymentOutputPort
     */
    public final OutputPort<IUndeployedEvent> getUndeployedOutputPort() {
        return this.undeployedOutputPort;
    }

    /**
     * @return the allocationOutputPort
     */
    public final OutputPort<IAllocationEvent> getAllocationOutputPort() {
        return this.allocationOutputPort;
    }

    /**
     * @return the flowOutputPort
     */
    public final OutputPort<IFlowRecord> getFlowOutputPort() {
        return this.flowOutputPort;
    }

    /**
     *
     * @return traceOutputPort
     */
    public OutputPort<TraceMetadata> getTraceMetadataOutputPort() {
        return this.traceMetadataOutputPort;
    }

    /**
     *
     * @return sessionEventPort
     */
    public OutputPort<ISessionEvent> getSessionEventOutputPort() {
        return this.sessionEventOutputPort;
    }

    public long getRecordCount() {
        return this.recordCount;
    }

    public OutputPort<IDeallocationEvent> getDeallocationOutputPort() {
        return this.deallocationOutputPort;
    }
}