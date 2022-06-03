/* **************************************************************************************
 * Copyright (c) 2022 Calypso Networks Association https://calypsonet.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
package org.eclipse.keyple.plugin.stub.spi;

import org.eclipse.keyple.plugin.stub.StubSmartCard;

/**
 * This interface must be used to provide an APDU response according to an APDU request. It is used
 * by {@link StubSmartCard} to delegate it APDU response providing to another class.
 *
 * @since 2.1.0
 */
public interface ApduResponseProviderSpi {
  /**
   * Provide APDU responses according to an APDU request.
   *
   * @param apduRequest hexadecimal string format (without spaces)
   * @return the APDU response in hexadecimal string format (without spaces)
   * @since 2.1.0
   */
  String getResponseFromRequest(String apduRequest);
}
