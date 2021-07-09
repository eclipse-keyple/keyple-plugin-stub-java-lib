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
import org.eclipse.keyple.core.util.Assert;
import org.eclipse.keyple.plugin.stub.StubPoolPluginFactoryAdapter.StubPoolReaderConfiguration;

/**
 * Builds instances of {@link StubPoolPluginFactory}.
 *
 * @since 2.0
 */
public final class StubPoolPluginFactoryBuilder {

  /**
   * The plugin default name
   *
   * @since 2.0
   */
  public static String PLUGIN_NAME = "StubPoolPlugin";

  private StubPoolPluginFactoryBuilder() {}

  /**
   * Creates builder to build a {@link StubPoolPluginFactory}.
   *
   * @return created builder
   * @since 2.0
   */
  public static StubPoolPluginFactoryBuilder.Builder builder() {
    return new StubPoolPluginFactoryBuilder.Builder();
  }

  /**
   * Builder to build a {@link StubPoolPluginFactory}.
   *
   * @since 2.0
   */
  public static class Builder {

    private final Set<StubPoolReaderConfiguration> readerConfigurations;
    private int monitoringCycleDuration;

    /** (private) Constructs an empty Builder. */
    private Builder() {
      readerConfigurations = new HashSet<StubPoolReaderConfiguration>();
      monitoringCycleDuration = 0;
    }

    /**
     * Initialize the plugin with a stub reader. Multiple readers can be added. The reader can
     * contain a smart card. Readers name should be unique.
     *
     * @param name name for the initially inserted reader (not nullable and unique)
     * @param card (optional) inserted smart card
     * @return instance of the builder
     * @since 2.0
     */
    public StubPoolPluginFactoryBuilder.Builder withStubReader(
        String groupReference, String name, StubSmartCard card) {
      Assert.getInstance().notNull(name, "Stub Reader name");
      readerConfigurations.add(new StubPoolReaderConfiguration(groupReference, name, card));
      return this;
    }

    /**
     * Configure the sleep time between two cycles for card and reader monitoring
     *
     * @param duration in milliseconds, default value : 0
     * @return instance of the builder
     * @since 2.0
     */
    public StubPoolPluginFactoryBuilder.Builder withMonitoringCycleDuration(int duration) {
      this.monitoringCycleDuration = duration;
      return this;
    }

    /**
     * Returns an instance of StubPoolPluginFactory created from the fields set on this builder.
     *
     * @return A {@link StubPoolPluginFactory}
     * @since 2.0
     */
    public StubPoolPluginFactory build() {
      return new StubPoolPluginFactoryAdapter(
          PLUGIN_NAME, readerConfigurations, monitoringCycleDuration);
    }
  }
}
