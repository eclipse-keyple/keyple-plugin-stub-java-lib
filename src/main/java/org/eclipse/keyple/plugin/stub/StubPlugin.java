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

import org.eclipse.keyple.core.common.KeyplePluginExtension;

import java.util.Set;

/**
 * Stub specific {@link KeyplePluginExtension}.
 *
 * @since 2.0
 */
public interface StubPlugin extends KeyplePluginExtension {
  /**
   * Plug a new {@link StubReader} available in the plugin
   *
   * @param name name of the created reader
   * @param synchronous should the stubreader added synchronously (without waiting for the
   *     observation thread). A READER_CONNECTED event is raised in both cases
   * @since 1.0
   */
  void plugReader(String name, Boolean synchronous);

  /**
   * Plug a new {@link StubReader} available in the plugin
   *
   * @param name name of the created reader
   * @param isContactless true if the created reader should be contactless, false if not.
   * @param synchronous should the stubreader added synchronously (without waiting for the
   *     observation thread). A READER_CONNECTED event is raised in both cases
   * @since 1.0
   */
  void plugReader(String name, boolean isContactless, Boolean synchronous);

  /**
   * Plug multiple new {@link StubReader} available in the plugin
   *
   * @param names names of readers to be connected
   * @param synchronous should the stubreader be added synchronously (without waiting for the
   *     observation thread). A READER_CONNECTED event is raised in both cases
   * @since 1.0
   */
  void plugReaders(Set<String> names, Boolean synchronous);

  /**
   * Unplug a {@link StubReader}
   *
   * @param name the name of the reader
   * @throws IllegalStateException in case of a reader exception
   * @param synchronous should the stubreader be removed synchronously (without waiting for the
   *     observation thread). A READER_DISCONNECTED event is raised in both cases
   * @since 1.0
   */
  void unplugReader(String name, Boolean synchronous);

  /**
   * Unplug a list of {@link StubReader}
   *
   * @param names names of the reader to be unplugged
   * @param synchronous should the stubreader removed synchronously (without waiting for the
   *     observation thread). A READER_DISCONNECTED event is raised in both cases
   * @since 1.0
   */
  void unplugReaders(Set<String> names, Boolean synchronous);
}
