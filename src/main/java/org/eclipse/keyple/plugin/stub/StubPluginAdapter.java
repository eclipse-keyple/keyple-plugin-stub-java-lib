package org.eclipse.keyple.plugin.stub;

import org.eclipse.keyple.core.plugin.PluginIOException;
import org.eclipse.keyple.core.plugin.spi.ObservablePluginSpi;
import org.eclipse.keyple.core.plugin.spi.reader.ReaderSpi;
import org.eclipse.keyple.core.util.Assert;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class StubPluginAdapter implements StubPlugin, ObservablePluginSpi {

  String name;
  Map<String, StubReaderAdapter> stubReaders;

  StubPluginAdapter(String name, Set<StubReaderConfiguration> readerConfigurations){
    this.name = name;
    stubReaders = new HashMap<>();
    for(StubReaderConfiguration configuration : readerConfigurations){
      this.plugReader(configuration.getName(), configuration.getContactless(), configuration.getCard());
    }
  }

  @Override
  public int getMonitoringCycleDuration() {
    return 0;
  }

  @Override
  public Set<String> searchAvailableReadersNames() throws PluginIOException {
    return stubReaders.keySet();
  }

  @Override
  public ReaderSpi searchReader(String readerName) throws PluginIOException {
    return stubReaders.get(readerName);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Set<ReaderSpi> searchAvailableReaders() throws PluginIOException {
    return new HashSet(stubReaders.values());
  }

  @Override
  public void unregister() {
    //NO-OP
  }

  @Override
  public void plugReader(String name, boolean isContactless, StubSmartCard card) {
    Assert.getInstance().notNull(name, "reader name");
    stubReaders.put(name, new StubReaderAdapter(name,isContactless, card));
  }

  @Override
  public void unplugReader(String name) {
    Assert.getInstance().notNull(name, "reader name");
    stubReaders.remove(name);
  }

}
