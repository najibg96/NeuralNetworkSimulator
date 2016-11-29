package neuralnetworksimulator;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.ArrayRealVector;

/**
 * <h1>Neural network sample </h1>
 * Training/testing sample instance for any kind of neural network.
 * Gives fields to register the last error information connected to this sample.
 * @author Najib
 */
public class NNSample {
    public double error_sum; //difference between output and result
    public double ms_error; //squared error divided by output length
    public ArrayRealVector error; //distance between output and result
    public ArrayRealVector input;
    public ArrayRealVector output;
    private ArrayRealVector sample; //full length sample

    /**
     * Creates the sample vector by splitting the line parameter.
     * @param line
     */
    public NNSample(String line) {
        String[] split = line.split(",");
        double[] sample_arr = new double[split.length];
        for (int i = 0; i < split.length; i++) {
            sample_arr[i] = Double.parseDouble(split[i]);
        }
        sample = new ArrayRealVector(sample_arr);
    }

    /**
     * Creates the input part
     * @param index - starting index
     * @param inputdim - length of vector
     * @throws OutOfRangeException
     * @throws NotPositiveException
     */
    public void setIn(int index, int inputdim)throws OutOfRangeException,NotPositiveException {
        input =  (ArrayRealVector) sample.getSubVector(index,inputdim);
    }

    /**
     * Creates the output part
     * @param index - starting index
     * @param outputdim - length of vector
     * @throws OutOfRangeException
     * @throws NotPositiveException
     */
    public void  setOut(int index, int outputdim) throws OutOfRangeException,NotPositiveException {
        output = (ArrayRealVector) sample.getSubVector(index,outputdim);
    }

}

