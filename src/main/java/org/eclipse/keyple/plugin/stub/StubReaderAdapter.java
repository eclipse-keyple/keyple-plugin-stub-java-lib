package org.eclipse.keyple.plugin.stub;

import org.eclipse.keyple.core.plugin.CardIOException;
import org.eclipse.keyple.core.plugin.ReaderIOException;
import org.eclipse.keyple.core.plugin.spi.reader.observable.ObservableReaderSpi;
import org.eclipse.keyple.core.plugin.spi.reader.observable.state.processing.WaitForCardRemovalDuringProcessingSpi;
import org.eclipse.keyple.core.plugin.spi.reader.observable.state.removal.WaitForCardRemovalBlockingSpi;
import org.eclipse.keyple.core.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * The adapter for the StubReader is also an ObservableReaderSpi
 */
final class StubReaderAdapter implements StubReader,
        ObservableReaderSpi {

    private static final Logger logger = LoggerFactory.getLogger(StubReaderAdapter.class);

    private final String name;
    private final Boolean isContactLess;
    private final Set<String> readerProtocols;

    private StubSmartCard smartCard;

    StubReaderAdapter(String name,Boolean isContactLess, StubSmartCard card){
        this.name = name;
        this.isContactLess = isContactLess;
        this.readerProtocols = new HashSet<String>();
        this.smartCard = card;
    }

    /*
     * ObservableReaderSpi
     */
    @Override
    public void onStartDetection() {
        if (logger.isTraceEnabled())
            logger.trace("Detection has been started on reader {}", this.getName());
    }

    @Override
    public void onStopDetection() {
        if (logger.isTraceEnabled())
            logger.trace("Detection has been stopped on reader {}", this.getName());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isProtocolSupported(String readerProtocol) {
        return true;//do not block any protocol
    }

    @Override
    public void activateProtocol(String readerProtocol) {
        readerProtocols.add(readerProtocol);
    }

    @Override
    public void deactivateProtocol(String readerProtocol) {
        readerProtocols.remove(readerProtocol);
    }

    @Override
    public boolean isCurrentProtocol(String readerProtocol) {
        if (smartCard != null && smartCard.getCardProtocol() != null) {
            return smartCard.getCardProtocol().equals(readerProtocol);
        } else {
            return false;
        }
    }

    @Override
    public void openPhysicalChannel(){
        if (smartCard != null) {
            smartCard.openPhysicalChannel();
        }
    }

    @Override
    public void closePhysicalChannel(){
        if (smartCard != null) {
            smartCard.closePhysicalChannel();
        }
    }

    @Override
    public boolean isPhysicalChannelOpen() {
        return smartCard != null && smartCard.isPhysicalChannelOpen();
    }

    @Override
    public boolean checkCardPresence()  {
        return smartCard != null;
    }

    @Override
    public byte[] getAtr() {
        return smartCard.getATR();
    }

    @Override
    public byte[] transmitApdu(byte[] apduIn) throws CardIOException {
        if (smartCard == null) {
            throw new CardIOException("No card available.");
        }
        return smartCard.processApdu(apduIn);
    }

    @Override
    public boolean isContactless() {
        return isContactLess;
    }

    @Override
    public void unregister() {
        //NO-OP
    }

    /*
     * StubReader
     */

    @Override
    public void insertCard(StubSmartCard smartCard) {
        Assert.getInstance().notNull(smartCard, "smart card");
        if (logger.isTraceEnabled())
            logger.trace("Insert card {}", smartCard);

        /* clean channels status */
        if (isPhysicalChannelOpen()) {
            closePhysicalChannel();
        }
        this.smartCard = smartCard;
    }

    @Override
    public void removeCard() {
        if(smartCard !=null){
            if (logger.isTraceEnabled())
                logger.trace("Remove card {}", smartCard);
            closePhysicalChannel();
            this.smartCard = null;
        }
    }

    @Override
    public StubSmartCard getSmartcard() {
        return smartCard;
    }
}
