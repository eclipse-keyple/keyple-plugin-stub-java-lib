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

import org.eclipse.keyple.core.plugin.PluginIOException;
import org.junit.Before;
import org.junit.Test;

public class StubPluginFactoryAdapterTest {

  StubPluginFactoryAdapter factory;
  StubSmartCard card;
  String READER_NAME = "test";

  @Before
  public void setup() {
    card = getCard();
  }

  @Test
  public void init_factory_with_reader_configuration() throws PluginIOException {
    factory =
        (StubPluginFactoryAdapter)
            StubPluginFactoryBuilder.builder()
                .withStubReader(READER_NAME, true, card)
                .withMonitoringCycleDuration(100)
                .build();

    StubPluginAdapter stubPlugin = (StubPluginAdapter) factory.getPlugin();
    assertThat(stubPlugin).isNotNull();
    assertThat(stubPlugin.searchAvailableReaders().size()).isEqualTo(1);

    StubReaderAdapter reader = (StubReaderAdapter) stubPlugin.searchReader(READER_NAME);
    assertThat(reader).isNotNull();
    assertThat(reader.getSmartcard()).isEqualTo(card);
    assertThat(reader.isContactless()).isTrue();
  }
}
