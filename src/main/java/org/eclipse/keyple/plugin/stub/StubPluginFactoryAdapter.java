/* **************************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://www.calypsonet-asso.org/
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

import static org.eclipse.keyple.plugin.stub.StubPlugin.PLUGIN_NAME;

import java.util.Set;
import org.eclipse.keyple.core.common.CommonsApiProperties;
import org.eclipse.keyple.core.plugin.PluginApiProperties;
import org.eclipse.keyple.core.plugin.spi.PluginFactorySpi;
import org.eclipse.keyple.core.plugin.spi.PluginSpi;

/**
 * (package-private)<br>
 * Factory of {@link StubPlugin}.
 *
 * @since 2.0
 */
final class StubPluginFactoryAdapter implements StubPluginFactory, PluginFactorySpi {

  final Set<StubReaderConfiguration> readerConfigurations;
  final int monitoringCycleDuration;

  /**
   * (package-private)<br>
   * Creates an instance, sets the fields from the factory builder.
   *
   * @since 2.0
   */
  StubPluginFactoryAdapter(
      Set<StubReaderConfiguration> readerConfigurations, int monitoringCycleDuration) {
    this.readerConfigurations = readerConfigurations;
    this.monitoringCycleDuration = monitoringCycleDuration;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public String getPluginApiVersion() {
    return PluginApiProperties.VERSION;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public String getCommonsApiVersion() {
    return CommonsApiProperties.VERSION;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public String getPluginName() {
    return PLUGIN_NAME;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public PluginSpi getPlugin() {
    return new StubPluginAdapter(PLUGIN_NAME, readerConfigurations, monitoringCycleDuration);
  }

  /** (package-private) Internal configuration for an initially plugged stubReader */
  static class StubReaderConfiguration {

    private final String name;
    private final Boolean isContactless;
    private final StubSmartCard card;

    StubReaderConfiguration(String name, Boolean isContactless, StubSmartCard card) {
      this.name = name;
      this.isContactless = isContactless;
      this.card = card;
    }

    String getName() {
      return name;
    }

    Boolean getContactless() {
      return isContactless;
    }

    StubSmartCard getCard() {
      return card;
    }
  }
}
