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

import org.eclipse.keyple.core.plugin.CardIOException;
import org.eclipse.keyple.core.util.ByteArrayUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Simulated smart card that can be inserted into a {@link StubReader}. Register apdu responses with the method {@link #addHexCommand(String, String)}  
 */
public class StubSmartCard {

  protected byte[] atr;
  protected String cardProtocol;
  protected boolean isPhysicalChannelOpen = false;
  protected Map<String, String> hexCommands = new ConcurrentHashMap<String, String>();

  /**
   * Create a simulated smart card with mandatory parameters
   * @param atr (non nullable) atr of the card
   * @param cardProtocol (non nullable) card protocol
   */
  public StubSmartCard(byte[] atr, String cardProtocol){
    this.atr = atr;
    this.cardProtocol = cardProtocol;
  };


  /**
   * Gets the card protocol supported by the card
   * @return A not empty String.
   */
  public String getCardProtocol(){return cardProtocol;};

  /**
   * Getter for ATR
   * @return Secured Element ATR
   */
  public byte[] getATR(){return atr;};

  public boolean isPhysicalChannelOpen() {
    return isPhysicalChannelOpen;
  }

  public void openPhysicalChannel() {
    this.isPhysicalChannelOpen = true;
  }

  public void closePhysicalChannel() {
    this.isPhysicalChannelOpen = false;
  }


  /**
   * Add more simulated commands to the card Stub
   *
   * @param command hexadecimal command to react to
   * @param response hexadecimal response to be sent in reaction to command
   */
  public void addHexCommand(String command, String response) {
    if (command == null || response == null) {
      throw new IllegalArgumentException("Command and Response should not be null");
    }
    // add commands without space
    hexCommands.put(command.replace(" ", ""), response.replace(" ", ""));
  }

  /**
   * Remove simulated commands from the card Stub
   *
   * @param command hexadecimal command to be removed
   */
  public void removeHexCommand(String command) {
    if (command == null) {
      throw new IllegalArgumentException("Command should not be null");
    }
    hexCommands.remove(command.trim());
  }

  /**
   * Return APDU Response to APDU Request
   *
   * @param apduIn commands to be processed
   * @return APDU response
   */
  public byte[] processApdu(byte[] apduIn) throws CardIOException {

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
