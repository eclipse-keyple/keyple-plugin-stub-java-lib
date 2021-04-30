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

import static org.eclipse.keyple.plugin.stub.StubPluginFactoryAdapter.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.eclipse.keyple.core.plugin.PluginIOException;
import org.eclipse.keyple.core.plugin.spi.ObservablePluginSpi;
import org.eclipse.keyple.core.plugin.spi.reader.ReaderSpi;
import org.eclipse.keyple.core.util.Assert;

/** Internal adapter of the {@link StubPlugin} */
class StubPluginAdapter implements StubPlugin, ObservablePluginSpi {

  private final String name;
  private final int monitoringCycleDuration;
  private final Map<String, StubReaderAdapter> stubReaders;

  /**
   * (package-private )constructor
   *
   * @param name name of the plugin
   * @param readerConfigurations configuraitons of the reader to plug initially
   * @param monitoringCycleDuration duration between two monitoring cycle
   */
  StubPluginAdapter(
      String name, Set<StubReaderConfiguration> readerConfigurations, int monitoringCycleDuration) {
    this.name = name;
    this.monitoringCycleDuration = monitoringCycleDuration;
    this.stubReaders = new HashMap<String, StubReaderAdapter>();
    for (StubReaderConfiguration configuration : readerConfigurations) {
      this.plugReader(
          configuration.getName(), configuration.getContactless(), configuration.getCard());
    }
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public int getMonitoringCycleDuration() {
    return monitoringCycleDuration;
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public Set<String> searchAvailableReadersNames() throws PluginIOException {
    return stubReaders.keySet();
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public ReaderSpi searchReader(String readerName) throws PluginIOException {
    return stubReaders.get(readerName);
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public String getName() {
    return name;
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public Set<ReaderSpi> searchAvailableReaders() throws PluginIOException {
    return new HashSet(stubReaders.values());
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void unregister() {
    // NO-OP
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void plugReader(String name, boolean isContactless, StubSmartCard card) {
    Assert.getInstance().notNull(name, "reader name");
    stubReaders.put(name, new StubReaderAdapter(name, isContactless, card));
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void unplugReader(String name) {
    Assert.getInstance().notNull(name, "reader name");
    stubReaders.remove(name);
  }
}
