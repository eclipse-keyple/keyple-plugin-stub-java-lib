package org.eclipse.keyple.plugin.stub;

import org.junit.Before;
import org.junit.Test;

public class StubPluginAdapterTest {

    private StubPluginAdapter adapter;
    private final String NAME = "name";
    @Before
    public void setup(){
        adapter = new StubPluginAdapter(NAME,null);
    }

    @Test
    public void plugReader_should_Create_reader(){
        adapter.plugReader("test", true,null);
    }
}
