package neuralnetworksimulator;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <h1>Neural network sample database</h1>
 * Training/testing sample database implementation for arbitrary neural network implementation.
 * Lets us load samples from a file in cvs text format (comma separation) and divide the samples into
 * input and output vectors.
 * Reusable class
 * Uses {@link ArrayList} as a base class
 * @see org.apache.commons.math3.linear.ArrayRealVector
 * @author Najib
 */
public class NNSampleDatabase extends ArrayList<NNSample> {

    /**
     * Opens databaseFile and loads all samples intro this array of samples.
     * @param databaseFile
     */
    public NNSampleDatabase(File databaseFile) {
        super();
        try {
            BufferedReader sample_file = new BufferedReader(new FileReader(databaseFile));
            String line;
            while (true) {
                line = sample_file.readLine();
                if (line == null)
                    break;
                add(new NNSample(line));
            }
            sample_file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Divides all samples in the database into inputdim and outputdim long vectors
     * constructing input and output {@link org.apache.commons.math3.linear.ArrayRealVector}
     * public attribute of {@link NNSample}.
     * @param inputdim
     * @param outputdim
     * @return boolean - success or fail
     */
    public boolean setIODimensions(int inputdim, int outputdim){

        try {
            for(NNSample sample : this){
                sample.setIn(0, inputdim);
                sample.setOut(inputdim,outputdim);
            }
        }catch (OutOfRangeException e){
            e.printStackTrace();
            return false;
        } catch (NotPositiveException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
