package code.com.recursion;

import com.recursion.Recursion;
import org.junit.Test;

public class RecursionTest {

    @Test
    public void testCollatzRecursion() {
        Recursion.collatzRecursion(6255042);
    }

    @Test
    public void testCollatzTailRecursion() {
        Recursion.collatzTailRecursion(6255042);
    }
}
