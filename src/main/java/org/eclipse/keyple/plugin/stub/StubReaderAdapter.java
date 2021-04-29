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

import java.util.HashSet;
import java.util.Set;
import org.eclipse.keyple.core.plugin.CardIOException;
import org.eclipse.keyple.core.plugin.spi.reader.observable.ObservableReaderSpi;
import org.eclipse.keyple.core.plugin.spi.reader.observable.state.insertion.WaitForCardInsertionNonBlockingSpi;
import org.eclipse.keyple.core.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The adapter for the StubReader is also an ObservableReaderSpi */
final class StubReaderAdapter
    implements StubReader, ObservableReaderSpi, WaitForCardInsertionNonBlockingSpi {

  private static final Logger logger = LoggerFactory.getLogger(StubReaderAdapter.class);

  private final String name;
  private final Boolean isContactLess;
  private final Set<String> readerProtocols;

  private StubSmartCard smartCard;

  /**
   * (package-private) constructor
   *
   * @param name name of the reader
   * @param isContactLess true if contactless
   * @param card (optionnal) inserted smart card at creation
   */
  StubReaderAdapter(String name, Boolean isContactLess, StubSmartCard card) {
    this.name = name;
    this.isContactLess = isContactLess;
    this.readerProtocols = new HashSet<String>();
    this.smartCard = card;
  }

  /*
   * ObservableReaderSpi
   */
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void onStartDetection() {
    if (logger.isTraceEnabled())
      logger.trace("Detection has been started on reader {}", this.getName());
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void onStopDetection() {
    if (logger.isTraceEnabled())
      logger.trace("Detection has been stopped on reader {}", this.getName());
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public String getName() {
    return name;
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public boolean isProtocolSupported(String readerProtocol) {
    return true; // do not block any protocol
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void activateProtocol(String readerProtocol) {
    readerProtocols.add(readerProtocol);
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void deactivateProtocol(String readerProtocol) {
    readerProtocols.remove(readerProtocol);
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public boolean isCurrentProtocol(String readerProtocol) {
    if (smartCard != null && smartCard.getCardProtocol() != null) {
      return smartCard.getCardProtocol().equals(readerProtocol);
    } else {
      return false;
    }
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void openPhysicalChannel() {
    if (smartCard != null) {
      smartCard.openPhysicalChannel();
    }
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void closePhysicalChannel() {
    if (smartCard != null) {
      smartCard.closePhysicalChannel();
    }
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public boolean isPhysicalChannelOpen() {
    return smartCard != null && smartCard.isPhysicalChannelOpen();
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public boolean checkCardPresence() {
    return smartCard != null;
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public byte[] getAtr() {
    return smartCard.getATR();
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public byte[] transmitApdu(byte[] apduIn) throws CardIOException {
    if (smartCard == null) {
      throw new CardIOException("No card available.");
    }
    return smartCard.processApdu(apduIn);
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public boolean isContactless() {
    return isContactLess;
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void unregister() {
    // NO-OP
  }

  /*
   * StubReader
   */
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void insertCard(StubSmartCard smartCard) {
    Assert.getInstance().notNull(smartCard, "smart card");
    if (logger.isTraceEnabled()) logger.trace("Insert card {}", smartCard);

    /* clean channels status */
    if (isPhysicalChannelOpen()) {
      closePhysicalChannel();
    }
    this.smartCard = smartCard;
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void removeCard() {
    if (smartCard != null) {
      if (logger.isTraceEnabled()) logger.trace("Remove card {}", smartCard);
      closePhysicalChannel();
      this.smartCard = null;
    }
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public StubSmartCard getSmartcard() {
    return smartCard;
  }
}
