/* **************************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://calypsonet.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
package org.eclipse.keyple.plugin.stub;

import java.util.Set;
import org.eclipse.keyple.core.common.CommonApiProperties;
import org.eclipse.keyple.core.plugin.PluginApiProperties;
import org.eclipse.keyple.core.plugin.spi.PoolPluginFactorySpi;
import org.eclipse.keyple.core.plugin.spi.PoolPluginSpi;
import org.eclipse.keyple.plugin.stub.StubPluginFactoryAdapter.StubReaderConfiguration;

/**
 * (package-private)<br>
 * Factory of {@link StubPoolPlugin}.
 *
 * @since 2.0.0
 */
final class StubPoolPluginFactoryAdapter implements StubPoolPluginFactory, PoolPluginFactorySpi {

  private final Set<StubPoolReaderConfiguration> readerConfigurations;
  private final int monitoringCycleDuration;
  private final String pluginName;

  /**
   * (package-private)<br>
   * Creates an instance, sets the fields from the factory builder.
   *
   * @param pluginName name of the plugin
   * @param readerConfigurations readerConfigurations to be created at init
   * @param monitoringCycleDuration duration of each monitoring cycle
   * @since 2.0.0
   */
  StubPoolPluginFactoryAdapter(
      String pluginName,
      Set<StubPoolReaderConfiguration> readerConfigurations,
      int monitoringCycleDuration) {
    this.pluginName = pluginName;
    this.readerConfigurations = readerConfigurations;
    this.monitoringCycleDuration = monitoringCycleDuration;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public String getPluginApiVersion() {
    return PluginApiProperties.VERSION;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public String getCommonApiVersion() {
    return CommonApiProperties.VERSION;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public String getPoolPluginName() {
    return pluginName;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public PoolPluginSpi getPoolPlugin() {
    return new StubPoolPluginAdapter(pluginName, readerConfigurations, monitoringCycleDuration);
  }

  static class StubPoolReaderConfiguration extends StubReaderConfiguration {

    private final String groupReference;

    /**
     * (package-private) constructor for a reader configuration
     *
     * @param groupReference groupReference of the reader (not nullable)
     * @param name name of the reader (not nullable)
     * @param card inserted card (nullable)
     * @since 2.0.0
     */
    StubPoolReaderConfiguration(String groupReference, String name, StubSmartCard card) {
      super(name, false, card);
      this.groupReference = groupReference;
    }

    String getGroupReference() {
      return groupReference;
    }
  }
}
