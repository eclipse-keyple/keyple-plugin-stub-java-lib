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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.keyple.core.plugin.CardIOException;
import org.eclipse.keyple.core.plugin.TaskCanceledException;
import org.eclipse.keyple.core.plugin.spi.reader.observable.ObservableReaderSpi;
import org.eclipse.keyple.core.plugin.spi.reader.observable.state.insertion.WaitForCardInsertionNonBlockingSpi;
import org.eclipse.keyple.core.plugin.spi.reader.observable.state.processing.WaitForCardRemovalDuringProcessingBlockingSpi;
import org.eclipse.keyple.core.plugin.spi.reader.observable.state.removal.WaitForCardRemovalNonBlockingSpi;
import org.eclipse.keyple.core.util.Assert;
import org.eclipse.keyple.core.util.ByteArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * (package-private)<br>
 * The adapter for the StubReader is also an ObservableReaderSpi
 *
 * @since 2.0
 */
final class StubReaderAdapter
    implements StubReader,
        ObservableReaderSpi,
        WaitForCardInsertionNonBlockingSpi,
        WaitForCardRemovalDuringProcessingBlockingSpi,
        WaitForCardRemovalNonBlockingSpi {

  private static final Logger logger = LoggerFactory.getLogger(StubReaderAdapter.class);

  private final String name;
  private final Boolean isContactLess;
  private final Set<String> activatedProtocols;

  private StubSmartCard smartCard;
  private final AtomicBoolean continueWaitForCardRemovalTask = new AtomicBoolean();

  /**
   * (package-private)<br>
   * constructor
   *
   * @param name name of the reader
   * @param isContactLess true if contactless
   * @param card (optional) inserted smart card at creation
   * @since 2.0
   */
  StubReaderAdapter(String name, Boolean isContactLess, StubSmartCard card) {
    this.name = name;
    this.isContactLess = isContactLess;
    this.activatedProtocols = new HashSet<String>();
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
    activatedProtocols.add(readerProtocol);
  }
  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void deactivateProtocol(String readerProtocol) {
    activatedProtocols.remove(readerProtocol);
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
  public String getPowerOnData() {
    return ByteArrayUtil.toHex(smartCard.getPowerOnData());
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
  public void onUnregister() {
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
    if (checkCardPresence()) {
      logger.warn("You must remove the inserted card before inserted another one");
      return;
    }

    if (!activatedProtocols.contains(smartCard.getCardProtocol())) {
      if (logger.isTraceEnabled()) {
        logger.trace(
            "Inserted card protocol {} does not match any activated protocol, please use activateProtocol() method",
            smartCard.getCardProtocol());
      }
      return;
    }

    if (logger.isTraceEnabled()) logger.trace("Inserted card {}", smartCard);

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

  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void waitForCardRemovalDuringProcessing() throws TaskCanceledException {
    while (smartCard != null
        && continueWaitForCardRemovalTask.get()
        && !Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
    if (!continueWaitForCardRemovalTask.get()) {
      throw new TaskCanceledException("Wait for card removal task cancelled");
    }
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public void stopWaitForCardRemovalDuringProcessing() {
    continueWaitForCardRemovalTask.set(false);
  }
}
