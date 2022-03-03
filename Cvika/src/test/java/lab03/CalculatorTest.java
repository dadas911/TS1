package lab03;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorTest {
    static Calculator c;

    @BeforeAll
    public static void initVariable() {
        c = new Calculator();
    }

    @Test
    public void add_isTen_True() {
        Assertions.assertEquals(10, c.add(4, 6));
    }

    @Test
    public void subtract_isZero_True() {
        Assertions.assertEquals(0, c.subtract(20, 20));
    }

    @Test

    public void multiply_isTwenty_True() {
        Assertions.assertEquals(20, c.multiply(4, 5));
    }

    @Test
    public void divide_isThree_True() {
        Assertions.assertEquals(3, c.divide(9, 3));
    }

    @Test
    void divideByZero() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> c.divide(1, 0));
        Assertions.assertEquals("Cannot divide by zero!", exception.getMessage());
    }
}
