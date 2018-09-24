import org.junit.Test;

import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void getType() {
        Message message = new Message("Hello");
        assertEquals("Hello", message.getType());
    }

    @Test
    public void getField() {
        Message message = new Message("Status,1,0,0,0,0,0");
            assertEquals("Status,1,0,0,0,0,0",message.getField());
    }
}