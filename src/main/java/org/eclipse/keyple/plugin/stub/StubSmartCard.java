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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.eclipse.keyple.core.plugin.CardIOException;
import org.eclipse.keyple.core.util.Assert;
import org.eclipse.keyple.core.util.ByteArrayUtil;

/**
 * Simulated smart card that can be inserted into a {@link StubReader}. Use the {@link Builder} to
 * create this object
 */
public class StubSmartCard {

  private final byte[] atr;
  private final String cardProtocol;
  private boolean isPhysicalChannelOpen;
  private final Map<String, String> hexCommands;

  /**
   * (private) Create a simulated smart card with mandatory parameters
   *
   * @param atr (non nullable) atr of the card
   * @param cardProtocol (non nullable) card protocol
   * @param hexCommands (non nullable) set of simulated commands
   */
  private StubSmartCard(byte[] atr, String cardProtocol, Map<String, String> hexCommands) {
    this.atr = atr;
    this.cardProtocol = cardProtocol;
    this.hexCommands = hexCommands;
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

    if (apduIn == null || apduIn.length == 0) {
      return new byte[0];
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

  @Override
  public String toString() {
    return "StubSmartCard{"
        + "atr="
        + ByteArrayUtil.toHex(atr)
        + ", cardProtocol='"
        + cardProtocol
        + '\''
        + ", isPhysicalChannelOpen="
        + isPhysicalChannelOpen
        + ", hexCommands(#)="
        + hexCommands.size()
        + '}';
  }

  public static AtrStep builder() {
    return new Builder();
  }

  /**
   * Builder class for {@link StubSmartCard}.
   *
   * @since 2.0
   */
  public static class Builder implements AtrStep, ProtocolStep, CommandStep {
    private byte[] atr;
    private String cardProtocol;
    private Map<String, String> hexCommands;

    private Builder() {
      hexCommands = new HashMap<String, String>();
    }

    @Override
    public Builder withHexCommands(String command, String response) {
      Assert.getInstance().notNull(command, "command").notNull(response, "response");
      // add commands without space
      hexCommands.put(command.trim(), response.trim());
      return this;
    }

    @Override
    public StubSmartCard build() {
      return new StubSmartCard(atr, cardProtocol, hexCommands);
    }

    @Override
    public ProtocolStep withAtr(byte[] atr) {
      this.atr = atr;
      return this;
    }

    @Override
    public Builder withProcotol(String protocol) {
      Assert.getInstance().notNull(protocol, "Protocol");
      this.cardProtocol = protocol;
      return this;
    }
  }

  interface AtrStep {
    /**
     * Define Atr for the {@link StubSmartCard} to build
     *
     * @param atr (not nullable) byte of array
     * @return next step of builder
     * @since 2.0
     */
    ProtocolStep withAtr(byte[] atr);
  }

  interface ProtocolStep {
    /**
     * Define protocol for the {@link StubSmartCard} to build
     *
     * @param protocol (not nullable) protocol name
     * @return next step of builder
     * @since 2.0
     */
    CommandStep withProcotol(String protocol);
  }

  interface CommandStep {
    /**
     * Add simulated command/response for the {@link StubSmartCard} to build
     *
     * @param command hexadecimal command to respond to (can be a regexp to match multiple apdu)
     * @param response hexadecimal response
     * @return next step of builder
     * @since 2.0
     */
    CommandStep withHexCommands(String command, String response);

    /**
     * Build the {@link StubSmartCard}
     *
     * @return new instance a StubSmartCard
     */
    StubSmartCard build();
  }
}
