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
import static org.eclipse.keyple.plugin.stub.StubSmartCardTest.*;

import org.eclipse.keyple.core.plugin.CardIOException;
import org.eclipse.keyple.core.util.ByteArrayUtil;
import org.junit.Before;
import org.junit.Test;

public class StubReaderAdapterTest {

  StubReaderAdapter adapter;
  final String NAME = "name";
  final String PROTOCOL = "any";
  Boolean IS_CONTACT_LESS = true;
  StubSmartCard card = buildCard(PROTOCOL);

  @Before
  public void setup() {
    adapter = new StubReaderAdapter(NAME, IS_CONTACT_LESS, null);
  }

  @Test
  public void test_constructor() {
    assertThat(adapter.getName()).isEqualTo(NAME);
    assertThat(adapter.isContactless()).isEqualTo(IS_CONTACT_LESS);
    assertThat(adapter.isProtocolSupported("any")).isTrue();
  }

  @Test(expected = IllegalArgumentException.class)
  public void insertCard_withNull_shouldThrow_Ex() {
    adapter.insertCard(null);
  }

  @Test
  public void insert_card_with_activated_protocol() throws CardIOException {
    adapter.activateProtocol(PROTOCOL);
    adapter.insertCard(card);
    assertThat(adapter.getSmartcard()).isEqualTo(card);
    assertThat(adapter.getPowerOnData()).isEqualTo(ByteArrayUtil.toHex(card.getPowerOnData()));
    assertThat(adapter.isPhysicalChannelOpen()).isEqualTo(card.isPhysicalChannelOpen());
    assertThat(adapter.checkCardPresence()).isTrue();
    assertThat(adapter.isCurrentProtocol(PROTOCOL)).isTrue();
    assertThat(adapter.transmitApdu(ByteArrayUtil.fromHex(commandHex)))
        .isEqualTo(ByteArrayUtil.fromHex(responseHex));
  }

  @Test
  public void insert_card_without_activated_protocol_return_null() throws CardIOException {
    adapter.activateProtocol(PROTOCOL);
    adapter.deactivateProtocol(PROTOCOL);
    adapter.insertCard(card);
    assertThat(adapter.getSmartcard()).isNull();
  }

  @Test
  public void remove_inserted_card() throws CardIOException {
    adapter.activateProtocol(PROTOCOL);
    adapter.insertCard(card);
    assertThat(adapter.getSmartcard()).isEqualTo(card);
    adapter.removeCard();
    assertThat(adapter.getSmartcard()).isNull();
  }

  @Test
  public void insert_card_with_inserted_card_does_nothing() throws CardIOException {
    StubSmartCard card2 = buildCard(PROTOCOL);
    adapter.activateProtocol(PROTOCOL);
    adapter.insertCard(card);
    adapter.insertCard(card2);
    assertThat(adapter.getSmartcard()).isEqualTo(card);
  }

  @Test
  public void test_open_close_channel() throws CardIOException {
    adapter.activateProtocol(PROTOCOL);
    adapter.insertCard(card);
    assertThat(adapter.isPhysicalChannelOpen()).isFalse();
    adapter.openPhysicalChannel();
    assertThat(adapter.isPhysicalChannelOpen()).isTrue();
    adapter.closePhysicalChannel();
    assertThat(adapter.isPhysicalChannelOpen()).isFalse();
  }

  @Test(expected = CardIOException.class)
  public void op_without_card() throws CardIOException {
    assertThat(adapter.isCurrentProtocol("any")).isFalse();
    adapter.transmitApdu(ByteArrayUtil.fromHex(commandHex)); // trhow ex
  }

  private StubSmartCard buildCard(String protocol) {
    return StubSmartCard.builder()
        .withPowerOnData(ByteArrayUtil.fromHex("0000"))
        .withProtocol(protocol)
        .withSimulatedCommand(commandHex, responseHex)
        .build();
  }
}
