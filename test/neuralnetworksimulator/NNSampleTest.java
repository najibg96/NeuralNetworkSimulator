/**
 * 
 */
package neuralnetworksimulator;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Najib
 *
 */
public class NNSampleTest {

	NNSample s;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		s = new  NNSample("1.1,2.1,3.1,3,2,1");
	}

	@Test
	public void testSetIn() {
		s.setIn(0, 3);
		Assert.assertArrayEquals(new double[]{1.1,2.1,3.1},s.input.getDataRef(), 0.0001);
		
	}

	@Test
	public void testSetOut() {
		s.setOut(3, 3);
		Assert.assertArrayEquals(new double[]{3,2,1},s.output.getDataRef(), 0);
	}

}
