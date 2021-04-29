/* **************************************************************************************
 * Copyright (c) 2018 Calypso Networks Association https://www.calypsonet-asso.org/
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.eclipse.keyple.core.plugin.CardIOException;
import org.eclipse.keyple.core.util.Assert;
import org.eclipse.keyple.core.util.ByteArrayUtil;

/**
 * Simulated smart card that can be inserted into a {@link StubReader}. Register apdu responses with
 * the method {@link #addHexCommand(String, String)}
 */
public class StubSmartCard {

  private final byte[] atr;
  private final String cardProtocol;
  private boolean isPhysicalChannelOpen;
  private final Map<String, String> hexCommands;

  /**
   * Create a simulated smart card with mandatory parameters
   *
   * @param atr (non nullable) atr of the card
   * @param cardProtocol (non nullable) card protocol
   */
  public StubSmartCard(byte[] atr, String cardProtocol) {
    this.atr = atr;
    this.cardProtocol = cardProtocol;
    hexCommands = new ConcurrentHashMap<String, String>();
    isPhysicalChannelOpen = false;
  };

  /**
   * Gets the card protocol supported by the card
   *
   * @return A not empty String.
   * @since 2.0
   */
  String getCardProtocol() {
    return cardProtocol;
  };

  /**
   * Get the card ATR
   *
   * @return Secured Element ATR
   * @since 2.0
   */
  byte[] getATR() {
    return atr;
  };

  /**
   * Add simulated commands to the card Stub. A command can be a regexp to match multiple apdu.
   *
   * @param command hexadecimal command to react to, can be a regexp
   * @param response hexadecimal response to be sent in reaction to command
   * @since 2.0
   */
  public StubSmartCard addHexCommand(String command, String response) {
    Assert.getInstance().notNull(command, "command").notNull(response, "response");
    // add commands without space
    hexCommands.put(command.trim(), response.trim());
    return this;
  }

  /**
   * Remove simulated commands from the card Stub
   *
   * @param command hexadecimal command to be removed
   * @since 2.0
   */
  public StubSmartCard removeHexCommand(String command) {
    Assert.getInstance().notNull(command, "command");
    hexCommands.remove(command.trim());
    return this;
  }

  /**
   * Get the status of the physical channel
   *
   * @return true if the physical channel is open
   * @since 2.0
   */
  boolean isPhysicalChannelOpen() {
    return isPhysicalChannelOpen;
  }

  /**
   * Open the physical channel of the card
   *
   * @since 2.0
   */
  void openPhysicalChannel() {
    this.isPhysicalChannelOpen = true;
  }

  /**
   * Close the physical channel of the card
   *
   * @since 2.0
   */
  void closePhysicalChannel() {
    this.isPhysicalChannelOpen = false;
  }

  /**
   * Return APDU Response to APDU Request
   *
   * @param apduIn commands to be processed
   * @return APDU response
   * @since 2.0
   */
  byte[] processApdu(byte[] apduIn) throws CardIOException {

    if (apduIn == null) {
      return null;
    }

    // convert apduIn to hexa
    String hexApdu = ByteArrayUtil.toHex(apduIn);

    // return matching hexa response if the provided APDU matches the regex
    Pattern p;
    for (Map.Entry<String, String> hexCommand : hexCommands.entrySet()) {
      p = Pattern.compile(hexCommand.getKey());
      if (p.matcher(hexApdu).matches()) {
        return ByteArrayUtil.fromHex(hexCommand.getValue());
      }
    }

    // throw a CardIOException if not found
    throw new CardIOException("No response available for this request: " + hexApdu);
  }
}
