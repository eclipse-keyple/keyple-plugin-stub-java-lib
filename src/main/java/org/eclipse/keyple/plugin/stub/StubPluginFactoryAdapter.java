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

import org.eclipse.keyple.core.common.CommonsApiProperties;
import org.eclipse.keyple.core.plugin.PluginApiProperties;
import org.eclipse.keyple.core.plugin.spi.PluginFactorySpi;
import org.eclipse.keyple.core.plugin.spi.PluginSpi;

import java.util.Set;

/**
 * (package-private)<br>
 * Factory of {@link StubPlugin}.
 *
 * @since 2.0
 */
final class StubPluginFactoryAdapter implements StubPluginFactory, PluginFactorySpi {

  /**
   * (package-private)<br>
   * The plugin name
   *
   * @since 2.0
   */
  static final String PLUGIN_NAME = "StubPlugin";
  Set<StubReaderConfiguration> readerConfigurations;
  /**
   * (package-private)<br>
   * Creates an instance, sets the fields from the factory builder.
   *
   * @since 2.0
   */
  StubPluginFactoryAdapter(Set<StubReaderConfiguration> readerConfigurations) {
    this.readerConfigurations = readerConfigurations;
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
    return new StubPluginAdapter(PLUGIN_NAME, readerConfigurations);
  }
}
