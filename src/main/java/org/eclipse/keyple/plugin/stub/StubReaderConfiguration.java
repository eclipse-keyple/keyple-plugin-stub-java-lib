package org.eclipse.keyple.plugin.stub;

/**
 * (package-private)
 * Internal immutable configuration for an initially plugged stubReader
 */
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

    StubSmartCard getCard(){
        return card;
    }
}
