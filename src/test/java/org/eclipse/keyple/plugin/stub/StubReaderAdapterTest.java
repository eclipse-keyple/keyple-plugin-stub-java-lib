package org.eclipse.keyple.plugin.stub;

import org.eclipse.keyple.plugin.stub.StubReaderAdapter;
import org.junit.Before;
import org.junit.Test;

public class StubReaderAdapterTest {

    private StubReaderAdapter adapter;
    private final String NAME = "name";
    @Before
    public void setup(){
        adapter = new StubReaderAdapter(NAME, true,null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertCard_withNull_shouldThrow_Ex(){
        adapter.insertCard(null);
    }
}
