package lab03;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FooTest {
    static Foo f;

    @BeforeAll
    public static void initVariable() {
        f = new Foo();
    }

    @Test
    public void returnNumber_isFive_True()
    {
        Assertions.assertEquals(5, f.returnNumber());
    }

    @Test
    public void returnNumber_isFour_True()
    {
        Assertions.assertEquals(4, f.returnNumber());
    }

}
