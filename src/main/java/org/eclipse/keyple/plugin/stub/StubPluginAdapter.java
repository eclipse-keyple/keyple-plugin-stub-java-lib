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

import static org.eclipse.keyple.plugin.stub.StubPluginFactoryAdapter.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.keyple.core.plugin.spi.ObservablePluginSpi;
import org.eclipse.keyple.core.plugin.spi.reader.ReaderSpi;
import org.eclipse.keyple.core.util.Assert;

/**
 * (package-private)<br>
 * Internal adapter of the {@link StubPlugin}
 *
 * @since 2.0
 */
class StubPluginAdapter implements StubPlugin, ObservablePluginSpi {

  private final String name;
  private final int monitoringCycleDuration;
  private final Map<String, StubReaderAdapter> stubReaders;

  /**
   * (package-private )constructor
   *
   * @param name name of the plugin
   * @param readerConfigurations configurations of the reader to plug initially
   * @param monitoringCycleDuration duration between two monitoring cycles
   * @since 2.0
   */
  StubPluginAdapter(
      String name,
      Set<? extends StubReaderConfiguration> readerConfigurations,
      int monitoringCycleDuration) {
    this.name = name;
    this.monitoringCycleDuration = monitoringCycleDuration;
    this.stubReaders = new ConcurrentHashMap<String, StubReaderAdapter>();
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
  public Set<String> searchAvailableReaderNames() {
    return new HashSet<String>(stubReaders.keySet());
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public ReaderSpi searchReader(String readerName) {
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
  public Set<ReaderSpi> searchAvailableReaders() {
    return new HashSet<ReaderSpi>(stubReaders.values());
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void onUnregister() {
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
