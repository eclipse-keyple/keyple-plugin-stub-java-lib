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

import org.eclipse.keyple.core.plugin.CardIOException;
import org.eclipse.keyple.core.util.HexUtil;
import org.eclipse.keyple.plugin.stub.spi.ApduResponseProviderSpi;
import org.junit.Before;
import org.junit.Test;

public class StubSmartCardTest {

  static StubSmartCard card;
  static byte[] powerOnData = new byte[1];
  static String protocol = "protocol";
  static String commandHex = "1234567890ABCDEFFEDCBA0987654321";
  static String commandHexRegexp = "1234.*";
  static String responseHex = "response";

  @Before
  public void setup() {
    card = buildACard();
  }

  @Test
  public void sendApdu_apduExists_sendResponse() throws CardIOException {
    byte[] apduResponse = card.processApdu(HexUtil.toByteArray(commandHex));
    assertThat(apduResponse).isEqualTo(HexUtil.toByteArray(responseHex));
  }

  @Test
  public void sendApdu_apduRegexpExists_sendResponse() throws CardIOException {
    card =
        StubSmartCard.builder()
            .withPowerOnData(powerOnData)
            .withProtocol(protocol)
            .withSimulatedCommand(commandHexRegexp, responseHex)
            .build();
    byte[] apduResponse = card.processApdu(HexUtil.toByteArray(commandHex));
    assertThat(apduResponse).isEqualTo(HexUtil.toByteArray(responseHex));
  }

  @Test(expected = CardIOException.class)
  public void sendApdu_apduNotExists_sendException() throws CardIOException {
    card.processApdu(HexUtil.toByteArray("excp"));
  }

  @Test
  public void shouldUse_a_apduResponseProvider_to_sendResponse() throws CardIOException {
    card =
        StubSmartCard.builder()
            .withPowerOnData(powerOnData)
            .withProtocol(protocol)
            .withApduResponseProvider(
                new ApduResponseProviderSpi() {
                  @Override
                  public String getResponseFromRequest(String apduRequest) {
                    return (apduRequest.equals(commandHex)) ? responseHex : null;
                  }
                })
            .build();
    byte[] apduResponse = card.processApdu(HexUtil.toByteArray(commandHex));
    assertThat(apduResponse).isEqualTo(HexUtil.toByteArray(responseHex));
  }

  @Test
  public void open_close_physical_channel() {
    assertThat(card.isPhysicalChannelOpen()).isFalse();
    card.openPhysicalChannel();
    assertThat(card.isPhysicalChannelOpen()).isTrue();
    card.closePhysicalChannel();
    assertThat(card.isPhysicalChannelOpen()).isFalse();
  }

  static StubSmartCard buildACard() {
    return StubSmartCard.builder()
        .withPowerOnData(powerOnData)
        .withProtocol(protocol)
        .withSimulatedCommand(commandHex, responseHex)
        .build();
  }
}
