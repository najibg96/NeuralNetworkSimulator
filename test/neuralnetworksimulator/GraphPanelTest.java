package neuralnetworksimulator;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Najib
 *
 */
public class GraphPanelTest {
	GraphPanel g;
	ArrayList<Double> l;

	@Before
	public void setUp() throws Exception {
		l = new ArrayList<>();
		g = new GraphPanel(l);
		g.addScore(0.1);
		g.addScore(0.2);
		g.addScore(0.3);
	}
	
	@Test
	public void testGetScores(){
		Assert.assertEquals(l, g.getScores() );
	}

	@Test
	public void testAddValues() {
		Object[] result = g.getScores().toArray();
		Assert.assertArrayEquals(new Object[]{0.1,0.2,0.3}, result);
	}
	
	@Test
	public void testMinScore(){
		Assert.assertEquals(0.1, g.getMinScore(), 0);
	}

	@Test
	public void testMaxScore(){
		Assert.assertEquals(0.3, g.getMaxScore(), 0);
	}
	
}
