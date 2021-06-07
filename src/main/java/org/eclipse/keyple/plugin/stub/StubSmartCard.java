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
 *
 * @since 2.0
 */
public class StubSmartCard {

  private final byte[] powerOnData;
  private final String cardProtocol;
  private boolean isPhysicalChannelOpen;
  private final Map<String, String> hexCommands;

  /**
   * (private) <br>
   * Create a simulated smart card with mandatory parameters
   *
   * @param powerOnData (non nullable) power-on data of the card
   * @param cardProtocol (non nullable) card protocol
   * @param hexCommands (non nullable) set of simulated commands
   * @since 2.0
   */
  private StubSmartCard(byte[] powerOnData, String cardProtocol, Map<String, String> hexCommands) {
    this.powerOnData = powerOnData;
    this.cardProtocol = cardProtocol;
    this.hexCommands = hexCommands;
    isPhysicalChannelOpen = false;
  }

  /**
   * (package-private) <br>
   * Gets the card protocol supported by the card
   *
   * @return A not empty String.
   * @since 2.0
   */
  String getCardProtocol() {
    return cardProtocol;
  }

  /**
   * (package-private) <br>
   * Get the card power-on data
   *
   * @return Null if no power-on data are available.
   * @since 2.0
   */
  byte[] getPowerOnData() {
    return powerOnData;
  }

  /**
   * (package-private) <br>
   * Get the status of the physical channel
   *
   * @return True if the physical channel is open
   * @since 2.0
   */
  boolean isPhysicalChannelOpen() {
    return isPhysicalChannelOpen;
  }

  /**
   * (package-private) <br>
   * Open the physical channel of the card
   *
   * @since 2.0
   */
  void openPhysicalChannel() {
    this.isPhysicalChannelOpen = true;
  }

  /**
   * (package-private) <br>
   * Close the physical channel of the card
   *
   * @since 2.0
   */
  void closePhysicalChannel() {
    this.isPhysicalChannelOpen = false;
  }

  /**
   * (package-private) <br>
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

  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public String toString() {
    return "StubSmartCard{"
        + "powerOnData="
        + ByteArrayUtil.toHex(powerOnData)
        + ", cardProtocol='"
        + cardProtocol
        + '\''
        + ", isPhysicalChannelOpen="
        + isPhysicalChannelOpen
        + ", hexCommands(#)="
        + hexCommands.size()
        + '}';
  }

  /**
   * Creates a builder for the {@link StubSmartCard}
   *
   * @return Next step of the buider
   * @since 2.0
   */
  public static PowerOnDataStep builder() {
    return new Builder();
  }

  /**
   * Builder class for {@link StubSmartCard}.
   *
   * @since 2.0
   */
  public static class Builder implements PowerOnDataStep, ProtocolStep, CommandStep {
    private byte[] powerOnData;
    private String cardProtocol;
    private Map<String, String> hexCommands;

    private Builder() {
      hexCommands = new HashMap<String, String>();
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.0
     */
    @Override
    public Builder withSimulatedCommand(String command, String response) {
      Assert.getInstance().notNull(command, "command").notNull(response, "response");
      // add commands without space
      hexCommands.put(command.trim(), response.trim());
      return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.0
     */
    @Override
    public StubSmartCard build() {
      return new StubSmartCard(powerOnData, cardProtocol, hexCommands);
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.0
     */
    @Override
    public ProtocolStep withPowerOnData(byte[] powerOnData) {
      this.powerOnData = powerOnData;
      return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.0
     */
    @Override
    public Builder withProcotol(String protocol) {
      Assert.getInstance().notNull(protocol, "Protocol");
      this.cardProtocol = protocol;
      return this;
    }
  }

  public interface PowerOnDataStep {
    /**
     * Define simulated power-on data for the {@link StubSmartCard} to build
     *
     * @param powerOnData (not nullable) byte of array
     * @return next step of builder
     * @since 2.0
     */
    ProtocolStep withPowerOnData(byte[] powerOnData);
  }

  public interface ProtocolStep {
    /**
     * Define simulated protocol for the {@link StubSmartCard} to build
     *
     * @param protocol (not nullable) protocol name
     * @return next step of builder
     * @since 2.0
     */
    CommandStep withProcotol(String protocol);
  }

  public interface CommandStep {
    /**
     * Add simulated command/response to the {@link StubSmartCard} to build. Command and response
     * should be hexadecimal.
     *
     * @param command hexadecimal command to respond to (can be a regexp to match multiple apdu)
     * @param response hexadecimal response
     * @return next step of builder
     * @since 2.0
     */
    CommandStep withSimulatedCommand(String command, String response);

    /**
     * Build the {@link StubSmartCard}
     *
     * @return new instance a StubSmartCard
     */
    StubSmartCard build();
  }
}
