package mockitobug;

import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.mockito.Mockito.spy;

public class TestMockOutputStream {
    @Test
    public void runSpy() throws Exception {
        MyObjectOutputStream original = new MyObjectOutputStream();
        original.writeObject("hello"); // works
        MyObjectOutputStream spied = spy(original);
        spied.writeObject("hello"); // crashes
    }
}

class MyObjectOutputStream extends ObjectOutputStream {
    public MyObjectOutputStream() throws IOException {
        super();
    }
}
