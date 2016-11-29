package neuralnetworksimulator;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Najib
 *
 */
public class NeuralNetworkTest {
	
	NeuralNetwork n;
	
	@Before
	public void setUp() {
		n = new NeuralNetwork();
		n.setLayers("1,2,2"); 
		n.setWeights("0,1\n0,1\n0,0,1\n1,1,1");
		n.setEpochs(200);
	}

	@Test
	public void testSetLayers() {
		Assert.assertEquals(true, n.isSetLayers());
	}
	
	@Test
	public void testSetWeights() {
		Assert.assertEquals(true, n.isSetWeights());
	}
	
	@Test
	public void testSetEpochs() {
		Assert.assertEquals(200, n.getEpochs());
	}
	
	@Test
	public void testInputDim() {
		Assert.assertEquals(1, n.getInputDim());
	}
	
	@Test
	public void testOutputDim() {
		Assert.assertEquals(2, n.getOutputDim());
	}

}
