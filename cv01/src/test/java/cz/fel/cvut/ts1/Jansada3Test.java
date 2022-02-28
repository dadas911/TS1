package cz.fel.cvut.ts1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class Jansada3Test {

    @Test
    public void factorialTest()
    {
        Jansada3 username = new Jansada3();
        assertEquals(username.factorial(5), 120);
    }
}
