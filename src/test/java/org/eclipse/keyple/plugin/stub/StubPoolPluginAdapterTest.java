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

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.keyple.plugin.stub.StubSmartCardTest.buildACard;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.keyple.core.plugin.PluginIOException;
import org.eclipse.keyple.core.plugin.spi.reader.ReaderSpi;
import org.eclipse.keyple.plugin.stub.StubPoolPluginFactoryAdapter.StubPoolReaderConfiguration;
import org.junit.Before;
import org.junit.Test;

public class StubPoolPluginAdapterTest {

  private final String READER_NAME = "readerName";
  private final String READER_NAME_2 = "readerName2";

  private final String group1 = "group1";
  private final String group2 = "group2";
  private StubPoolPluginAdapter pluginPoolAdapter;
  private StubSmartCard card;
  private Set<StubPoolReaderConfiguration> readerConfigurations;

  @Before
  public void setUp() {
    readerConfigurations = new HashSet<StubPoolReaderConfiguration>();
    pluginPoolAdapter = new StubPoolPluginAdapter(READER_NAME, readerConfigurations, 0);
    card = buildACard();
  }

  @Test
  public void initPlugin_withMultipleReader() throws PluginIOException {
    readerConfigurations.add(new StubPoolReaderConfiguration(group1, READER_NAME, card));
    readerConfigurations.add(new StubPoolReaderConfiguration(group1, READER_NAME_2, card));

    pluginPoolAdapter = new StubPoolPluginAdapter(READER_NAME, readerConfigurations, 0);
    assertThat(pluginPoolAdapter.searchAvailableReaders()).hasSize(2);
    assertThat(pluginPoolAdapter.searchReader(READER_NAME).isContactless()).isFalse();
  }

  @Test
  public void plugReader_should_create_reader() throws PluginIOException {
    pluginPoolAdapter.plugPoolReader(group1, READER_NAME, card);
    assertThat(pluginPoolAdapter.searchAvailableReaders()).hasSize(1);
  }

  @Test
  public void allocate_reader_without_group() throws PluginIOException {
    initPlugin_withMultipleReader();
    ReaderSpi reader = pluginPoolAdapter.allocateReader(null);
    assertThat(reader.getName()).isIn(READER_NAME, READER_NAME_2);
  }

  @Test
  public void allocate_reader_with_group() throws PluginIOException {
    plugReader_should_create_reader();
    ReaderSpi reader = pluginPoolAdapter.allocateReader(group1);
    assertThat(reader.getName()).isEqualTo(READER_NAME);
  }

  @Test
  public void unplugReader_should_remove_reader() throws PluginIOException {
    plugReader_should_create_reader();
    pluginPoolAdapter.plugPoolReader(group2, READER_NAME_2, card);
    pluginPoolAdapter.unplugPoolReader(READER_NAME);
    assertThat(pluginPoolAdapter.searchAvailableReaders()).hasSize(1);
  }

  @Test
  public void unplugReaders_should_remove_readers() throws PluginIOException {
    plugReader_should_create_reader();
    pluginPoolAdapter.plugPoolReader(group1, READER_NAME_2, card);
    pluginPoolAdapter.unplugPoolReaders(group1);
    assertThat(pluginPoolAdapter.searchAvailableReaders()).isEmpty();
  }

  @Test(expected = PluginIOException.class)
  public void allocate_reader_on_group_when_no_reader_throw_ex() throws PluginIOException {
    allocate_reader_with_group();
    pluginPoolAdapter.allocateReader(group1); // throw ex
  }

  @Test(expected = PluginIOException.class)
  public void allocate_reader_when_no_reader_throw_ex() throws PluginIOException {
    pluginPoolAdapter.allocateReader(null); // throw ex
  }
}
