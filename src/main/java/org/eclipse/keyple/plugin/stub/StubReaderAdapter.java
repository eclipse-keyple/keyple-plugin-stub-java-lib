package org.eclipse.keyple.plugin.stub;

import org.eclipse.keyple.core.plugin.spi.reader.observable.ObservableReaderSpi;
import org.eclipse.keyple.core.plugin.spi.reader.observable.state.processing.WaitForCardRemovalDuringProcessingSpi;
import org.eclipse.keyple.core.plugin.spi.reader.observable.state.removal.WaitForCardRemovalBlockingSpi;

public class StubReaderAdapter implements StubReader,
        ObservableReaderSpi,
        WaitForCardRemovalDuringProcessingSpi,
        WaitForCardRemovalBlockingSpi {
}
