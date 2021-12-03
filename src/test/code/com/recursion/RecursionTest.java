package code.com.recursion;

import com.recursion.Recursion;
import org.junit.Test;

public class RecursionTest {

    @Test
    public void testCollatzRecursion() {
        int num = Recursion.collatzRecursion(6255042);
        assert num == 1;
    }

    @Test
    public void testCollatzTailRecursion() {
        Recursion.collatzTailRecursion(6255042);
    }
}
