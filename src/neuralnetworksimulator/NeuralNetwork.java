package neuralnetworksimulator;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import org.apache.commons.math3.linear.*;



/**
 * Reusable neural network implementation with
 * optimization for state-reporting functionality using {@link NNSimulatorViewController}.
 * Allows fast calculation of simple multilayer perceptron.
 * Uses the org.apache.commons.math3.linear library
 * @author Najib Ghadri
 */
public class NeuralNetwork {

	private int epochs; //total epochs in a run
	private double lambda_learning; //learing rate
	private double trainPercentage; //train/test percentage of samples

	private ArrayList<Layer> layers; //MLP
	private int inputDim;
	private int outputDim;
	private boolean isSetWeights = false;
	private boolean isSetLayers = false;

	/**
	 * Perceptron layer representation
	 */
	public class Layer {
		RealMatrix W; // weights
		RealMatrix d_W; // partial differed weights
		ArrayRealVector s; // sums
		ArrayRealVector y; // output
		ArrayRealVector b; // bias
		ArrayRealVector d_b; // partial differed bias
		ArrayRealVector dF; // partial forward derivative
	}

	/**
	 * Structure to exchange data with other classes
	 */
	public class NetworkData {
		ArrayList<Layer> layers;
		double minw, maxw;
		double mindw, maxdw;
	}

	///////////////////////////////////////////////////////////////////////////
	// Network architecture
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Construct the network architecture with zero values.
	 * @param line
	 */
	public void setLayers(String line) {
        String[] split = line.split(",");
        if(split.length == 0){
            return; // TODO: 17/11/2016 Validation
		}

        int i = 0;
        layers = new ArrayList<>();
        layers.add(new Layer()); // Input layer
        layers.get(i).y = new ArrayRealVector(Integer.parseInt(split[0]));
        inputDim = Integer.parseInt(split[0]);
        for (i = 1; i < split.length; i++) { // init hidden layers and Output
            layers.add(i, new Layer());
            int n = Integer.parseInt(split[i]);
            int m = layers.get(i - 1).y.getDimension();
            layers.get(i).b = new ArrayRealVector(n);
            layers.get(i).d_b = new ArrayRealVector(n);
            layers.get(i).dF = new ArrayRealVector(n);
            layers.get(i).s = new ArrayRealVector(n);
            layers.get(i).y = new ArrayRealVector(n);
            layers.get(i).W = new Array2DRowRealMatrix(n, m);
            layers.get(i).d_W = new Array2DRowRealMatrix(n, m);
        }
        outputDim = Integer.parseInt(split[split.length-1]);

		isSetWeights = false;
		isSetLayers = true;
    }

	/**
	 * @return - architecture is set?
	 */
	public boolean isSetLayers(){
		return isSetLayers;
	}

	/**
	 * Set network architecture. Values may all be 0-s.
	 * @return
	 */
	public ArrayList<Layer> getLayers(){
		return layers;
	}

	///////////////////////////////////////////////////////////////////////////
	// Weight values of the network
	///////////////////////////////////////////////////////////////////////////
	/**
	 * Initialize weight values of the network. If the paramater equals "random",
	 * every weight will be assigned a random value.
	 * @param input
	 */
	public void setWeights(String input) {
		if(input==null){
			isSetWeights = false;
			return; // TODO: 17/11/2016 Validation in swing
		}
        if(layers == null){
			isSetWeights = false;
            return; // TODO: 17/11/2016 Validation in swing
		}
        if(input.equals("random")){
            RandomVaules();
        } else{
			int i, j, k;
			try{
				int l = 0;
				//the network:
				String[] lines = input.split("\n");
				if(lines.length==0){
					return;
				}
				for (i = 1; i < layers.size(); i++) {
					//a layer:
					Layer layer = layers.get(i);
					double[] b = layer.b.getDataRef();
					for (j = 0; j < layer.W.getRowDimension(); j++) {
						//a neuron:
						String[] split = lines[l++].split(",");
						if(split.length==0){
							return;
						}
						for (k = 0; k < layer.W.getColumnDimension(); k++) {
							layer.W.setEntry(j, k, Double.parseDouble(split[k]));
						}
						b[j] = Double.parseDouble(split[k]);
					}
				}
			} catch (ArrayIndexOutOfBoundsException e){
				// TODO: 10/11/2016 Validation
			}
        }
		isSetWeights = true;
    }

	/**
	 *  Assign random values to all weights and biases using normal distribution centering 0, dispersing by 0.1.
	 */
	private void RandomVaules(){
		int i, j, k;
		Random rand = new Random();
		for (i = 1; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			double[] b = layer.b.getDataRef();
			for (j = 0; j < layer.W.getRowDimension(); j++) {
				for (k = 0; k < layer.W.getColumnDimension(); k++) {
					layer.W.setEntry(j, k, rand.nextGaussian() * 0.1);
				}
				b[j] = 0;
			}
		}
		return;
	}

	/**
	 * @return - weights are set?
	 */
	public boolean isSetWeights(){
		return isSetWeights;
	}

	/**
	 * @return - current network with weight data
	 */
	public NetworkData getCurrentNetworkData(){
		NetworkData data = new NetworkData();
		data.layers = layers;
		calculateWDWMinMax(data);

		return data;
	}

	///////////////////////////////////////////////////////////////////////////
	// Set learning parameters
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Set training sample percentage
	 * @param trainPercentage
	 */
	public void setTrainPercentage(double trainPercentage) {
		this.trainPercentage = trainPercentage;
	}

	/**
	 * Set number of epochs
	 * @param epochs
	 */
	public void setEpochs(int epochs) {
		this.epochs = epochs;
	}

	/**
	 * Set learning rate
	 * @param lambda_learning
	 */
	public void setLambda_learning(double lambda_learning) {
        this.lambda_learning = lambda_learning;
	}

	///////////////////////////////////////////////////////////////////////////
	// Get parameters
	///////////////////////////////////////////////////////////////////////////

	/**
	 * @return - sample percentage for training
	 */
	public double getTrainPercentage() {
		return trainPercentage;
	}

	/**
	 * @return - number of epochs
	 */
	public int getEpochs() {
		return epochs;
	}

	/**
	 * @return - input dimension
	 */
	public int getInputDim() {
		return inputDim;
	}

	/**
	 * Output dimension
	 * @return
	 */
	public int getOutputDim() {
		return outputDim;
	}

	/**
	 * save architecture to architecture.txt
	 * @throws FileNotFoundException
	 */
	public void exportArchitecture() throws FileNotFoundException {
        PrintStream os = new PrintStream(new File("architecture.txt"));

		String val = "";
		int i;
		for (i = 0; i < layers.size(); i++) {
			if (i != 0)
				val += ",";
			val += layers.get(i).y.getDimension();
		}
		os.println(val);

		for (i = 1; i < layers.size(); i++) {
			double[][] data = layers.get(i).W.getData();

			for (int j = 0; j < data.length; j++) {
				String line = "";
				for (int k = 0; k < data[j].length; k++) {
					if (k != 0)
						line += ",";
					line += new DecimalFormat("#0.000000000").format(data[j][k]);
				}
				line += "," + new DecimalFormat("#0.000000000").format(layers.get(i).b.getEntry(j));
				os.println(line);
			}
		}
		os.close();
	}


	/**
	 * Calculate the min and max value of all d_w-s and w-s in the network
	 * @param data
	 */
	public void calculateWDWMinMax(NetworkData data){
		int i, j, k;
		/*calculate max and min weight and derivative value*/
		data.maxw = layers.get(1).W.getEntry(0,0);
		data.minw = layers.get(1).W.getEntry(0,0);
		data.maxdw =layers.get(1).d_W.getEntry(0,0);
		data.mindw = layers.get(1).d_W.getEntry(0,0);
		for(i=1; i < layers.size(); i++){
			for(j = 0; j < layers.get(i).y.getDimension(); j++){
				for(k =0; k < layers.get(i-1).y.getDimension(); k++){
					double val;
					if((val = layers.get(i).W.getEntry(j,k)) > data.maxw)
						data.maxw = val;
					if((val = layers.get(i).W.getEntry(j,k)) < data.minw)
						data.minw = val;
					if((val = layers.get(i).d_W.getEntry(j,k)) > data.maxdw)
						data.maxdw = val;
					if((val = layers.get(i).d_W.getEntry(j,k)) < data.mindw)
						data.mindw = val;
				}
			}
		}
	}

	/**
	 * Calculates the output for an input, and calculates the error
	 * @param sample
	 */
	public void calculateOutputs(NNSample sample) {
		layers.get(0).y = sample.input.copy();
		for (int i = 1; i < layers.size(); i++) {
			Layer current_layer = layers.get(i);
			//sum input
			current_layer.s = new ArrayRealVector(current_layer.W.operate(layers.get(i - 1).y.copy().getDataRef()));
			// add bias
			current_layer.s = current_layer.s.add(current_layer.b);
			//activator function
			current_layer.y = current_layer.s.copy();
			if (i != layers.size() - 1) {
				double[] y = current_layer.y.getDataRef();
				for (int j = 0; j < y.length; j++) {
					y[j] = rectifier(y[j]);
				}
			}
		}
		/*Calculate error distance and mean squared error*/
		sample.error = sample.output.subtract(layers.get(layers.size() - 1).y);
		sample.error_sum = 0;
		sample.ms_error = 0;
		double[] error_a = sample.error.getDataRef();
		for (double val : error_a) {
			sample.error_sum += val; //error distance
			sample.ms_error += val * val; //squared error
		}
		sample.ms_error = sample.ms_error / error_a.length; //mean squared error
	}

	/**
	 * Update weights by the error of sample
	 * @param sample
	 */
	public void updateWeights(NNSample sample){
		calculateWDerivatives();
		calculateDeltaUpdate(sample);
	}

	/**
	 * Calculate partial derivatives for layer l
	 * @param l
	 * @return
	 */
	private ArrayRealVector calculateSigma(int l) {
		Layer current_layer = layers.get(l);

		if (l == layers.size() - 1) {
			current_layer.dF.mapMultiplyToSelf(0); // it is always one (multiplied with err later)
			current_layer.dF.mapAddToSelf(1);
		} else {
			current_layer.dF = (ArrayRealVector) layers.get(l + 1).W.preMultiply(calculateSigma(l + 1));
			double[] DF = current_layer.dF.getDataRef();
			for (int i = 0; i < DF.length; i++) {
				DF[i] *= rectifierD(current_layer.s.getEntry(i));
			}
		}
		return current_layer.dF;
	}

	/**
	 * Calculate derivatives using calculateSigma.
	 */
	private void calculateWDerivatives() {
		calculateSigma(1);
		int l;
		for (l = 1; l < layers.size(); l++) { // all layers
			layers.get(l).d_W = layers.get(l).dF.outerProduct(layers.get(l - 1).y);
			layers.get(l).d_b = layers.get(l).dF.copy();
		}
	}

	/**
	 * Update weights by adding the multiplication of the sample error with derivatives (roughly).
	 * @param sample
	 */
	private void calculateDeltaUpdate(NNSample sample) {
		int l;
		//hidden layers update
		for (l = 1; l < layers.size() - 1; l++) {
			Layer current = layers.get(l);
			current.W = current.W.add(current.d_W.scalarMultiply(sample.error_sum * 2 * lambda_learning));
			current.b = current.b.add(current.d_b.mapMultiply(lambda_learning * 2 * sample.error_sum));
		}
		// last layer
		Layer current = layers.get(l);
		double[] error = sample.error.getDataRef();
		for (int i = 0; i < current.d_W.getRowDimension(); i++) {
			for (int j = 0; j < current.d_W.getColumnDimension(); j++) {
				current.d_W.setEntry(i, j, current.d_W.getEntry(i, j)* error[i] * 2 * lambda_learning);
			}
		}
		current.W = current.W.add(current.d_W);
		current.b = current.b.add(current.d_b.ebeMultiply(sample.error).mapMultiply(lambda_learning * 2));
	}

	/**
	 * Rectifier function
	 * @param in
	 * @return
	 */
	private double rectifier(double in) {
		return in > 0 ? in : 0;
	}

	/**
	 * Rectifier first derivative function
	 * @param in
	 * @return
	 */
	private double rectifierD(double in) {
		return in > 0 ? 1 : 0;
	}
}