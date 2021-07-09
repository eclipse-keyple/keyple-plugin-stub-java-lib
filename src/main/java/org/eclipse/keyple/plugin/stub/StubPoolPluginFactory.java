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

import org.eclipse.keyple.core.common.KeyplePluginExtensionFactory;

/**
 * Stub specific {@link KeyplePluginExtensionFactory} to be provided to the Keyple SmartCard service
 * to register the Stub Pool plugin, built by {@link StubPoolPluginFactoryBuilder}.
 *
 * @since 2.0
 */
public interface StubPoolPluginFactory extends KeyplePluginExtensionFactory {}
