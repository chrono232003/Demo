package code.com.dotproduct;

import com.dotproduct.DotProduct;
import org.junit.Test;

import java.util.Arrays;
import java.util.Vector;

public class DotProductTest {
    @Test
    public void computeDotProductTest() {
        Vector<Integer> a = new Vector<>(Arrays.asList(2,4,6,8));
        Vector<Integer> b = new Vector<>(Arrays.asList(1,2,3,4));

        Vector<Integer> c = new Vector<>(Arrays.asList(10,0,2));
        Vector<Integer> d = new Vector<>(Arrays.asList(1,5,7));

        Integer result1 = DotProduct.computeDotProduct(a, b);
        Integer result2 = DotProduct.computeDotProduct(c, d);
        System.out.println(result1);
        System.out.println(result2);

        assert result1 == 60;
        assert result2 == 24;
    }
}
