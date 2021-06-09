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

import org.eclipse.keyple.core.common.KeyplePluginExtension;

/**
 * Specific methods of the Stub Plugin. Implemented as a {@link KeyplePluginExtension}. To invoke
 * those methods use Plugin#getExtension(StubPlugin.class) from the Plugin class.
 *
 * @since 2.0
 */
public interface StubPlugin extends KeyplePluginExtension {

  /**
   * Plug a new {@link StubReader}. This operation is asynchronous and will raise a READER_CONNECTED
   * event. A new {@link StubReader} is created and make available in the plugin
   *
   * @param name name for the reader (not nullable and unique)
   * @param isContactless true if the created reader should be contactless, false if not.
   * @param card created reader can contain a card (nullable)
   * @since 2.0
   */
  void plugReader(String name, boolean isContactless, StubSmartCard card);

  /**
   * Unplug a {@link StubReader}. This operation is asynchronous and will raise a
   * READER_DISCONNECTED event. The reader is removed from the * available list of reader. It does
   * nothing if no reader matches the name.
   *
   * @param name the name of the reader to unplug (not nullable)
   * @since 2.0
   */
  void unplugReader(String name);
}
