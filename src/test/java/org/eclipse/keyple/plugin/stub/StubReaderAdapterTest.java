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
import static org.eclipse.keyple.plugin.stub.StubSmartCardTest.*;

import org.eclipse.keyple.core.plugin.CardIOException;
import org.eclipse.keyple.core.util.ByteArrayUtil;
import org.junit.Before;
import org.junit.Test;

public class StubReaderAdapterTest {

  StubReaderAdapter adapter;
  final String NAME = "name";
  StubSmartCard card = getCard();

  @Before
  public void setup() {
    adapter = new StubReaderAdapter(NAME, true, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void insertCard_withNull_shouldThrow_Ex() {
    adapter.insertCard(null);
  }

  @Test
  public void insertCard_shouldBe_retrievable() throws CardIOException {
    adapter.insertCard(card);
    assertThat(adapter.getSmartcard()).isEqualTo(card);
    assertThat(adapter.getAtr()).isEqualTo(card.getATR());
    assertThat(adapter.isPhysicalChannelOpen()).isEqualTo(card.isPhysicalChannelOpen());
    assertThat(adapter.checkCardPresence()).isTrue();
    assertThat(adapter.isCurrentProtocol(card.getCardProtocol())).isTrue();
    assertThat(adapter.transmitApdu(ByteArrayUtil.fromHex(commandHex)))
        .isEqualTo(ByteArrayUtil.fromHex(responseHex));
  }
}
