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
package org.iobserve.service;

import java.io.File;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.configuration.ConfigurationFactory;

import teetime.framework.Configuration;
import teetime.framework.Execution;

import org.iobserve.analysis.ConfigurationException;

/**
 * Generic service main class.
 *
 * @param <T>
 *            type of the teetime Configuration to be used
 *
 * @author Reiner Jung
 *
 */
public abstract class AbstractServiceMain<T extends Configuration> {

    protected static final Log LOG = LogFactory.getLog(AbstractServiceMain.class);
    protected boolean help = false;

    /**
     * Configure and execute the evaluation tool.
     *
     * @param title
     *            start up label for debug messages
     * @param label
     *            label used during execution
     * @param args
     *            arguments are ignored
     */
    public void run(final String title, final String label, final String[] args) {
        AbstractServiceMain.LOG.debug(title);

        final JCommander commander = new JCommander(this);
        try {
            commander.parse(args);
            final kieker.common.configuration.Configuration configuration;
            if (this.getConfigurationFile() != null) {
                configuration = ConfigurationFactory
                        .createConfigurationFromFile(this.getConfigurationFile().getAbsolutePath());
            } else {
                configuration = null;
            }
            this.execute(configuration, commander, label);
        } catch (final ParameterException e) {
            AbstractServiceMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        } catch (final ConfigurationException e) {
            AbstractServiceMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        }
    }

    private void execute(final kieker.common.configuration.Configuration configuration, final JCommander commander,
            final String label) throws ConfigurationException {
        if (this.checkParameters(commander)) {
            if (this.help) {
                commander.usage();
                System.exit(1);
            } else {
                if (this.checkConfiguration(configuration, commander)) {
                    final Execution<T> execution = new Execution<>(this.createConfiguration(configuration));

                    this.shutdownHook(execution);

                    AbstractServiceMain.LOG.debug("Running " + label);

                    execution.executeBlocking();
                    this.shutdownService();

                    AbstractServiceMain.LOG.debug("Done");
                }
            }
        } else {
            AbstractServiceMain.LOG.error("Configuration Error");
        }
    }

    private <R extends Configuration> void shutdownHook(final Execution<R> execution) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (execution) {
                        execution.abortEventually();
                        AbstractServiceMain.this.shutdownService();
                    }
                } catch (final Exception e) { // NOCS

                }
            }
        }));

    }

    /**
     * Method returning the configuration file handle.
     *
     * @return returns a file handle in case a configuration file is used, else null
     */
    protected abstract File getConfigurationFile();

    /**
     * Check a given configuration for validity.
     *
     * @param configuration
     *            the configuration object with all configuration parameter. Can be null.
     * @param commander
     *            JCommander used to generate usage information.
     * @return true if the configuration is valid.
     */
    protected abstract boolean checkConfiguration(kieker.common.configuration.Configuration configuration,
            JCommander commander);

    /**
     * Create and initialize teetime configuration for a service.
     *
     * @param configuration
     *            kieker configuration object, can be null if now configuration
     * @return return the newly created service
     *
     * @throws ConfigurationException
     *             in case the creation fails
     */
    protected abstract T createConfiguration(kieker.common.configuration.Configuration configuration)
            throws ConfigurationException;

    /**
     * Check all given parameters for correct directory and files path, as well as, all other values
     * for fitness.
     *
     * @param commander
     *            the command line interface
     * @return true if all parameter check out, else false
     *
     * @throws ConfigurationException
     *             on error
     */
    protected abstract boolean checkParameters(JCommander commander) throws ConfigurationException;

    /**
     * Shutdown hook to run cleanup features of the application.
     */
    protected abstract void shutdownService();

}
