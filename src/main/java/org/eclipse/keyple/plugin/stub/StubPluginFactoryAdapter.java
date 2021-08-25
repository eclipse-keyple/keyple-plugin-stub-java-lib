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
import org.eclipse.keyple.core.plugin.spi.PluginFactorySpi;
import org.eclipse.keyple.core.plugin.spi.PluginSpi;

/**
 * (package-private)<br>
 * Factory of {@link StubPlugin}.
 *
 * @since 2.0.0
 */
final class StubPluginFactoryAdapter implements StubPluginFactory, PluginFactorySpi {

  private final Set<StubReaderConfiguration> readerConfigurations;
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
  StubPluginFactoryAdapter(
      String pluginName,
      Set<StubReaderConfiguration> readerConfigurations,
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
  public String getPluginName() {
    return pluginName;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public PluginSpi getPlugin() {
    return new StubPluginAdapter(pluginName, readerConfigurations, monitoringCycleDuration);
  }

  /**
   * (package-private)
   *
   * <p>Configuration for an initially plugged stubReader
   *
   * @since 2.0.0
   */
  static class StubReaderConfiguration {

    private final String name;
    private final boolean isContactless;
    private final StubSmartCard card;

    /**
     * (package-private) constructor for a reader configuration
     *
     * @param name name of the reader (not nullable)
     * @param isContactless true if the reader should be contactless (not nullable)
     * @param card inserted card (nullable)
     * @since 2.0.0
     */
    StubReaderConfiguration(String name, boolean isContactless, StubSmartCard card) {
      this.name = name;
      this.isContactless = isContactless;
      this.card = card;
    }

    /**
     * (package-private)<br>
     * Get the name of the reader
     *
     * @return not nullable name
     * @since 2.0.0
     */
    String getName() {
      return name;
    }

    /**
     * (package-private)<br>
     * Should the reader be contactless
     *
     * @return not nullable boolean
     * @since 2.0.0
     */
    boolean getContactless() {
      return isContactless;
    }

    /**
     * (package-private)<br>
     * Card inserted in the reader
     *
     * @return nullable object
     * @since 2.0.0
     */
    StubSmartCard getCard() {
      return card;
    }
  }
}
