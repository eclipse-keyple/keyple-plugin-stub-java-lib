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

import org.eclipse.keyple.core.plugin.CardIOException;
import org.eclipse.keyple.core.util.ByteArrayUtil;
import org.junit.Before;
import org.junit.Test;

public class StubSmartCardTest {

  static StubSmartCard card;
  static byte[] atr = new byte[1];
  static String protocol = "protocol";
  static String commandHex = "1234567890ABCDEFFEDCBA0987654321";
  static String commandHexRegexp = "1234.*";
  static String responseHex = "response";

  @Before
  public void setup() {
    card = new StubSmartCard(atr, protocol);
  }

  @Test
  public void sendApdu_adpuExists_sendResponse() throws CardIOException {
    card.addHexCommand(commandHex, responseHex);
    byte[] apduResponse = card.processApdu(ByteArrayUtil.fromHex(commandHex));
    assertThat(apduResponse).isEqualTo(ByteArrayUtil.fromHex(responseHex));
  }

  @Test
  public void sendApdu_adpuRegexpExists_sendResponse() throws CardIOException {
    card.addHexCommand(commandHexRegexp, responseHex);
    byte[] apduResponse = card.processApdu(ByteArrayUtil.fromHex(commandHex));
    assertThat(apduResponse).isEqualTo(ByteArrayUtil.fromHex(responseHex));
  }

  @Test(expected = CardIOException.class)
  public void sendApdu_adpuNotExists_sendException() throws CardIOException {
    card.addHexCommand(commandHex, responseHex);
    card.processApdu(ByteArrayUtil.fromHex("excp"));
  }

  @Test(expected = CardIOException.class)
  public void removeApdu_sendApudu_sendException() throws CardIOException {
    card.addHexCommand(commandHex, responseHex);
    card.removeHexCommand(commandHex);
    card.processApdu(ByteArrayUtil.fromHex("excp"));
  }

  @Test
  public void open_close_physical_channel() {
    assertThat(card.isPhysicalChannelOpen()).isFalse();
    card.openPhysicalChannel();
    assertThat(card.isPhysicalChannelOpen()).isTrue();
    card.closePhysicalChannel();
    assertThat(card.isPhysicalChannelOpen()).isFalse();
  }

  static StubSmartCard getCard() {
    return new StubSmartCard(atr, protocol).addHexCommand(commandHex, responseHex);
  }
}
