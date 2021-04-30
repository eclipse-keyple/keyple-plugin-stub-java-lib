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

import java.util.*;
import org.eclipse.keyple.core.util.Assert;

/**
 * Builds instances of {@link StubPluginFactory}.
 *
 * @since 2.0
 */
public final class StubPluginFactoryBuilder {

  private StubPluginFactoryBuilder() {}

  /**
   * Creates builder to build a {@link StubPluginFactory}.
   *
   * @return created builder
   * @since 2.0
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build a {@link StubPluginFactory}.
   *
   * @since 2.0
   */
  public static class Builder {

    private final Map<String, String> protocolRulesMap;
    private final Set<StubPluginFactoryAdapter.StubReaderConfiguration> readerConfigurations;
    private int monitoringCycleDuration;

    /** (private) Constructs an empty Builder. */
    private Builder() {
      protocolRulesMap = new HashMap<String, String>();
      readerConfigurations = new HashSet<StubPluginFactoryAdapter.StubReaderConfiguration>();
      monitoringCycleDuration = 0;
    }

    /**
     * Initialize the plugin with a stub reader. Multiple readers can be added. The reader can
     * contain a smart card. Readers name should be unique.
     *
     * @param name name for the initially inserted reader (not nullable and unique)
     * @param isContactLess true if the created reader should be contactless, false if not.
     * @param card (optional) inserted smart card
     * @return instance of the builder
     * @since 2.0
     */
    public Builder withStubReader(String name, boolean isContactLess, StubSmartCard card) {
      Assert.getInstance().notNull(name, "Stub Reader name");
      readerConfigurations.add(
          new StubPluginFactoryAdapter.StubReaderConfiguration(name, isContactLess, card));
      return this;
    }

    /**
     * Configure the sleep time between two cycles for card and reader monitoring
     *
     * @param duration in milliseconds, default value : 0
     * @return instance of the builder
     * @since 2.0
     */
    public Builder withMonitoringCycleDuration(int duration) {
      this.monitoringCycleDuration = duration;
      return this;
    }

    /**
     * Returns an instance of StubPluginFactory created from the fields set on this builder.
     *
     * @return A {@link StubPluginFactory}
     * @since 2.0
     */
    public StubPluginFactory build() {
      return new StubPluginFactoryAdapter(readerConfigurations, monitoringCycleDuration);
    }
  }
}
