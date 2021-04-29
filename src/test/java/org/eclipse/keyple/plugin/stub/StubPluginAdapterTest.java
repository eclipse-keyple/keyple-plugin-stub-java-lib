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

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.keyple.plugin.stub.StubSmartCardTest.getCard;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.keyple.core.plugin.PluginIOException;
import org.eclipse.keyple.core.plugin.ReaderIOException;
import org.junit.Before;
import org.junit.Test;

public class StubPluginAdapterTest {

  private StubPluginAdapter pluginAdapter;
  private StubSmartCard card;
  private final String NAME = "name";
  private Set<StubReaderConfiguration> readerConfigurations;

  @Before
  public void setup() {
    readerConfigurations = new HashSet<StubReaderConfiguration>();
    pluginAdapter = new StubPluginAdapter(NAME, readerConfigurations, 0);
    card = getCard();
  }

  @Test
  public void initPlugin_withOneReader() throws PluginIOException, ReaderIOException {
    readerConfigurations.add(new StubReaderConfiguration(NAME, true, card));
    pluginAdapter = new StubPluginAdapter(NAME, readerConfigurations, 0);
    assertThat(pluginAdapter.searchAvailableReaders().size()).isEqualTo(1);
    assertThat(pluginAdapter.searchAvailableReadersNames().size()).isEqualTo(1);
    assertThat(pluginAdapter.searchReader(NAME)).isNotNull();
    assertThat(pluginAdapter.searchReader(NAME).isContactless()).isTrue();
    assertThat(pluginAdapter.searchReader(NAME).checkCardPresence()).isTrue();
  }

  @Test
  public void plugReader_should_create_reader() throws PluginIOException {
    pluginAdapter.plugReader(NAME, true, card);
    assertThat(pluginAdapter.searchAvailableReaders().size()).isEqualTo(1);
  }

  @Test
  public void unplugReader_should_remove_reader() throws PluginIOException {
    pluginAdapter.plugReader(NAME, true, card);
    assertThat(pluginAdapter.searchAvailableReaders().size()).isEqualTo(1);
    pluginAdapter.unplugReader(NAME);
    assertThat(pluginAdapter.searchAvailableReaders().size()).isEqualTo(0);
  }
}
