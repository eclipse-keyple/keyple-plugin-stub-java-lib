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

/** (package-private) Internal immutable configuration for an initially plugged stubReader */
class StubReaderConfiguration {

  private final String name;
  private final Boolean isContactless;
  private final StubSmartCard card;

  StubReaderConfiguration(String name, Boolean isContactless, StubSmartCard card) {
    this.name = name;
    this.isContactless = isContactless;
    this.card = card;
  }

  String getName() {
    return name;
  }

  Boolean getContactless() {
    return isContactless;
  }

  StubSmartCard getCard() {
    return card;
  }
}
