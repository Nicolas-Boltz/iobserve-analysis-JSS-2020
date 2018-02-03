/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.stages.model;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.FileExtensionSwitch;
import teetime.stage.basic.merger.Merger;
import teetime.stage.basic.merger.strategy.NonBlockingFiniteRoundRobinStrategy;
import teetime.stage.io.Directory2FilesFilter;

public class ModelDir2ModelFilesStage extends CompositeStage {

    private static final Logger LOG = LogManager.getLogger(ModelDir2ModelFilesStage.class);

    private final Directory2FilesFilter directory2FilesFilter;
    private final FileExtensionSwitch fileExtensionSwitch;
    private final Merger<File> merger;

    public ModelDir2ModelFilesStage() {

        this.directory2FilesFilter = new Directory2FilesFilter(new Comparator<File>() {
            @Override
            public int compare(final File o1, final File o2) {
                try {
                    return o1.getCanonicalFile().compareTo(o2.getCanonicalFile());
                } catch (final IOException e) {
                    ModelDir2ModelFilesStage.LOG.error("Exception while getting canonical file name", e);
                    return 0;
                }
            }
        });

        // Filter only PCM model files from directory
        this.fileExtensionSwitch = new FileExtensionSwitch();

        final OutputPort<File> allocationOutputPort = this.fileExtensionSwitch.addFileExtension(".allocation");
        final OutputPort<File> cloudprofileOutputPort = this.fileExtensionSwitch.addFileExtension(".cloudprofile");
        final OutputPort<File> costOutputPort = this.fileExtensionSwitch.addFileExtension(".cost");
        final OutputPort<File> designdecisionOutputPort = this.fileExtensionSwitch.addFileExtension(".designdecision");
        final OutputPort<File> qmldeclarationOutputPort = this.fileExtensionSwitch.addFileExtension(".qmldeclarations");
        final OutputPort<File> repositoryOutputPort = this.fileExtensionSwitch.addFileExtension(".repository");
        final OutputPort<File> resourceenvironmentOutputPort = this.fileExtensionSwitch
                .addFileExtension(".resourceenvironment");
        final OutputPort<File> systemOutputPort = this.fileExtensionSwitch.addFileExtension(".system");
        final OutputPort<File> usagemodelOutputPort = this.fileExtensionSwitch.addFileExtension(".usagemodel");

        this.merger = new Merger<>(new NonBlockingFiniteRoundRobinStrategy());

        // Connect sub-stages
        this.connectPorts(this.directory2FilesFilter.getOutputPort(), this.fileExtensionSwitch.getInputPort());

        this.connectPorts(allocationOutputPort, this.merger.getNewInputPort());
        this.connectPorts(cloudprofileOutputPort, this.merger.getNewInputPort());
        this.connectPorts(costOutputPort, this.merger.getNewInputPort());
        this.connectPorts(designdecisionOutputPort, this.merger.getNewInputPort());
        this.connectPorts(qmldeclarationOutputPort, this.merger.getNewInputPort());
        this.connectPorts(repositoryOutputPort, this.merger.getNewInputPort());
        this.connectPorts(resourceenvironmentOutputPort, this.merger.getNewInputPort());
        this.connectPorts(systemOutputPort, this.merger.getNewInputPort());
        this.connectPorts(usagemodelOutputPort, this.merger.getNewInputPort());

    }

    public InputPort<File> getInputPort() {
        return this.directory2FilesFilter.getInputPort();
    }

    public OutputPort<File> getOutputPort() {
        return this.merger.getOutputPort();
    }
}