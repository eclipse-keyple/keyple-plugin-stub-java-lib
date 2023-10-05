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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.keyple.core.plugin.PluginIOException;
import org.eclipse.keyple.core.plugin.spi.ObservablePluginSpi;
import org.eclipse.keyple.core.plugin.spi.PoolPluginSpi;
import org.eclipse.keyple.core.plugin.spi.reader.PoolReaderSpi;
import org.eclipse.keyple.core.plugin.spi.reader.ReaderSpi;
import org.eclipse.keyple.core.util.Assert;
import org.eclipse.keyple.plugin.stub.StubPoolPluginFactoryAdapter.StubPoolReaderConfiguration;

/**
 * (package-private)<br>
 * Internal adapter of the {@link StubPoolPlugin}
 *
 * @since 2.0.0
 */
class StubPoolPluginAdapter implements StubPoolPlugin, PoolPluginSpi, ObservablePluginSpi {

  private final StubPluginAdapter stubPluginAdapter;
  private final Map<String, String> readerToGroup;
  private final Set<String> allocatedReaders; // list of allocated readers by their readerName

  /**
   * (package-private )constructor
   *
   * @param name name of the plugin
   * @param readerConfigurations configurations of the reader to plug initially
   * @param monitoringCycleDuration duration between two monitoring cycle
   * @since 2.0.0
   */
  StubPoolPluginAdapter(
      String name,
      Set<StubPoolReaderConfiguration> readerConfigurations,
      int monitoringCycleDuration) {
    this.stubPluginAdapter =
        new StubPluginAdapter(name, readerConfigurations, monitoringCycleDuration);
    this.readerToGroup = new ConcurrentHashMap<String, String>();
    this.allocatedReaders = new HashSet<String>();
    for (StubPoolReaderConfiguration readerConfiguration : readerConfigurations) {
      readerToGroup.put(readerConfiguration.getName(), readerConfiguration.getGroupReference());
    }
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public String getName() {
    return this.stubPluginAdapter.getName();
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public Set<ReaderSpi> searchAvailableReaders() {
    return this.stubPluginAdapter.searchAvailableReaders();
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public SortedSet<String> getReaderGroupReferences() {
    return new TreeSet<String>(readerToGroup.values());
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public PoolReaderSpi allocateReader(String readerGroupReference) throws PluginIOException {
    Set<String> candidateReadersName;

    if (readerGroupReference == null) {
      // every reader is candidate for allocation
      candidateReadersName = stubPluginAdapter.searchAvailableReaderNames();
    } else {
      // only readers from the readerGroupReference are candidates for allocation
      candidateReadersName = listReadersByGroup(readerGroupReference);
    }

    // find the first non allocated reader among candidates
    for (String readerName : candidateReadersName) {
      if (allocatedReaders.add(readerName)) {
        return (PoolReaderSpi) stubPluginAdapter.searchReader(readerName);
      }
    }

    throw new PluginIOException(
        "No reader is available in the groupReference : " + readerGroupReference);
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public void releaseReader(ReaderSpi readerSpi) {
    Assert.getInstance().notNull(readerSpi, "reader SPI");

    if (!(readerSpi instanceof StubReader)) {
      throw new IllegalArgumentException(
          "Can not release reader, Reader should be of type StubReader");
    }

    allocatedReaders.remove(readerSpi.getName());
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public void onUnregister() {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public void plugPoolReader(String groupReference, String readerName, StubSmartCard card) {
    Assert.getInstance()
        .notNull(groupReference, "group reference")
        .notNull(readerName, "reader name");

    // create new reader
    stubPluginAdapter.plugReader(readerName, false, card);

    // map reader to groupReference
    readerToGroup.put(readerName, groupReference);
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public void unplugPoolReaders(String groupReference) {
    Assert.getInstance().notNull(groupReference, "group reference");

    // find the reader in the readerPool
    Set<String> readerNames = listReadersByGroup(groupReference);
    for (String readerName : readerNames) {
      unplugPoolReader(readerName);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public void unplugPoolReader(String readerName) {
    Assert.getInstance().notNull(readerName, "reader name");

    // remove reader from pool
    readerToGroup.remove(readerName);

    // remove reader from allocate list
    allocatedReaders.remove(readerName);

    // remove reader from plugin
    stubPluginAdapter.unplugReader(readerName);
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public int getMonitoringCycleDuration() {
    return stubPluginAdapter.getMonitoringCycleDuration();
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public Set<String> searchAvailableReaderNames() {
    return stubPluginAdapter.searchAvailableReaderNames();
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public ReaderSpi searchReader(String readerName) {
    return stubPluginAdapter.searchReader(readerName);
  }

  /**
   * (private) lists all readers that match a group reference
   *
   * @param aGroupReference not nullable reference to a group reference
   * @return collection of reader names
   */
  private Set<String> listReadersByGroup(String aGroupReference) {
    Set<String> readers = new HashSet<String>();
    // find the reader in the readerPool
    for (Map.Entry<String, String> entry : readerToGroup.entrySet()) {
      String readerName = entry.getKey();
      String groupReference = entry.getValue();
      if (groupReference.equals(aGroupReference)) {
        readers.add(readerName);
      }
    }
    return readers;
  }
}
