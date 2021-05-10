/* **************************************************************************************
 * Copyright (c) 2019 Calypso Networks Association https://www.calypsonet-asso.org/
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

import org.eclipse.keyple.core.common.KeypleReaderExtension;

/**
 * The Stub Reader supports programmatically the insertion and removal of {@link StubSmartCard} <br>
 * To invoke those methods use Reader#getExtension(StubReader.class) from the Reader class.
 *
 * @since 2.0
 */
public interface StubReader extends KeypleReaderExtension {

  /**
   * Insert a stub card into the reader. The card is taken into account by the reader only if its
   * protocol has been activated previously by the method {@link
   * org.eclipse.keyple.core.plugin.spi.reader.ReaderSpi#activateProtocol(String)} In such case it
   * raises a CARD_INSERTED event.
   *
   * @param smartCard stub card to be inserted in the reader (not nullable)
   * @since 2.0
   */
  void insertCard(StubSmartCard smartCard);

  /**
   * Remove card from reader if any
   *
   * @since 2.0
   */
  void removeCard();

  /**
   * Get inserted card
   *
   * @return instance of a {@link StubSmartCard}, can be null if no card inserted
   * @since 2.0
   */
  StubSmartCard getSmartcard();
}
