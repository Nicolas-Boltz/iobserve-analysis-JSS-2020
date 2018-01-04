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
import org.iobserve.common.record.IAllocationRecord;
import org.iobserve.common.record.IDeallocationRecord;
import org.iobserve.common.record.IDeploymentRecord;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.common.record.IUndeploymentRecord;
import org.iobserve.common.record.ServerGeoLocation;
import org.iobserve.common.record.ServletTraceHelper;

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
    private final OutputPort<IDeploymentRecord> deploymentOutputPort = this.createOutputPort();
    /** output port for undeployment events. */
    private final OutputPort<IUndeploymentRecord> undeploymentOutputPort = this.createOutputPort();
    /** output port for allocation events. */
    private final OutputPort<IAllocationRecord> allocationOutputPort = this.createOutputPort();
    /** output port for deallocation events. */
    private final OutputPort<IDeallocationRecord> deallocationOutputPort = this.createOutputPort();

    /** output port for flow events. */
    private final OutputPort<IFlowRecord> flowOutputPort = this.createOutputPort();
    /** output port for {@link TraceMetadata}. */
    private final OutputPort<TraceMetadata> traceMetadataOutputPort = this.createOutputPort();

    private final OutputPort<ISessionEvent> sessionEventOutputPort = this.createOutputPort();
    /** output port for {@link ServerGeoLocation} */
    private final OutputPort<ServerGeoLocation> geoLocationOutputPort = this.createOutputPort();

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
        if (element instanceof IDeploymentRecord) {
            this.deploymentOutputPort.send((IDeploymentRecord) element);
        } else if (element instanceof ServerGeoLocation) {
            this.geoLocationOutputPort.send((ServerGeoLocation) element);
        } else if (element instanceof ISessionEvent) {
            this.sessionEventOutputPort.send((ISessionEvent) element);
        } else if (element instanceof IUndeploymentRecord) {
            this.undeploymentOutputPort.send((IUndeploymentRecord) element);
        } else if (element instanceof IAllocationRecord) {
            this.allocationOutputPort.send((IAllocationRecord) element);
        } else if (element instanceof IDeallocationRecord) {
            this.deallocationOutputPort.send((IDeallocationRecord) element);
        } else if (element instanceof ServletTraceHelper) { // NOCS
            // TODO this is later used to improve trace information
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
    public final OutputPort<IDeploymentRecord> getDeploymentOutputPort() {
        return this.deploymentOutputPort;
    }

    /**
     * @return the undeploymentOutputPort
     */
    public final OutputPort<IUndeploymentRecord> getUndeploymentOutputPort() {
        return this.undeploymentOutputPort;
    }

    /**
     * @return the allocationOutputPort
     */
    public final OutputPort<IAllocationRecord> getAllocationOutputPort() {
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

    /**
     * @return serverGeoLocationPort
     */
    public OutputPort<ServerGeoLocation> getGeoLocationOutputPort() {
        return this.geoLocationOutputPort;
    }

    public long getRecordCount() {
        return this.recordCount;
    }

    public OutputPort<IDeallocationRecord> getDeallocationOutputPort() {
        return this.deallocationOutputPort;
    }
}
