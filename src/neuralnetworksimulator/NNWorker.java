package neuralnetworksimulator;



import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;



/**
 * Neural network executor and Swing GUI controller made with Singleton pattern approach.
 * Runs the network process in a different thread than EDT.
 */
public class NNWorker extends SwingWorker<Boolean,NeuralNetwork.NetworkData> {

    ///////////////////////////////////////////////////////////////////////////
    // Static fields
    ///////////////////////////////////////////////////////////////////////////

    /*Singleton nnworker instance*/
    private static NNWorker process;

    /*Bridge to view modification*/
    private static NNSimulatorViewController controller;
    private static NeuralNetwork network;
    private static NNSampleDatabase samples; //sample database

    private static int delay; //simulation speed controled with delay

    /**
     * Set controller
     * @param controller
     */
    public static void setController(NNSimulatorViewController controller) {
        NNWorker.controller = controller;
    }

    /**
     * Set the network model
     * @param network
     */
    public static void setNetwork(NeuralNetwork network) {
        NNWorker.network = network;
    }

    /**
     * Set the sample database from a file
     * @param file
     */
    public static void setSampleDB(File file) {
        samples = new NNSampleDatabase(file);
    }

    /**
     * @return - sample database
     */
    public static NNSampleDatabase getSamples() {
        return samples;
    }

    /**
     * Start the process then pause and resume until stopped.
     * Locks the controlPanel and executes a new {@link NNWorker}
     */
    public static void startToggle(){
        if(process == null) {
            controller.updateUserInputs();
            controller.lockControl(true);
            controller.reportNNWorkerStatus(true);
            controller.resetErrorGraph();

            process = new NNWorker();
            process.execute();
        }
        else{
            pauseToggle();
        }
    }

    /**
     * Stopps the {@link NNWorker} process by setting stoop boolean.
     * Unlocks the controlPanel.
     */
    public static void stop(){
        if(process!= null) {
            if(process.paused)
                pauseToggle();
            process.stopped = true;
            process  = null;

            controller.lockControl(false);
            controller.reportNNWorkerStatus(false);
        }
    }

    /**
     * Pause resume. Used by startToggle()
     */
    private static void pauseToggle(){
        if(process!=null) {
            if (!process.paused) {
                synchronized (process){
                    process.paused = true;
                    controller.reportNNWorkerStatus(false);
                }
            } else {
                synchronized (process){
                    process.paused = false;
                    process.notify();
                    controller.reportNNWorkerStatus(true);
                }
            }
        }
    }

    /**
     * Delay setter
     * @param delay
     */
    public static void setDelay(int delay) {
        NNWorker.delay = delay;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Object fields
    ///////////////////////////////////////////////////////////////////////////

    /*so triviális (typo in "triviális". mennyéinnen intelidzsé, mitképzelsz)*/
    private boolean stopped = false;
    private boolean paused = false;

    /**
     * get orderer number set up to i
     * @param i
     * @return
     */
    private Integer[] getOrderedSet(int i){
        Integer[] arr = new Integer[i];
        for (int k = 0; k < arr.length; k++) {
            arr[k] = k;
        }
        return arr;
    }

    /**
     * shuffle the arr set randomly
     * @param arr
     * @return
     */
    private Integer[] getRandomSet(Integer[] arr){
        Integer[] randomSet = arr.clone();
        Collections.shuffle(Arrays.asList(randomSet));
        return randomSet;
    }

    /**
     * Neural network calculator process. For more look at documentation.
     * @return
     * @throws Exception
     */
    @Override
    public Boolean doInBackground() throws Exception {
        synchronized (network) {
            if(samples==null) {
                return false; // TODO: 17/11/2016 Validation
            }

            if(!samples.setIODimensions(network.getInputDim(),network.getOutputDim())){
                return false; // TODO: 17/11/2016 Validation
            }
            try {
                NeuralNetwork.NetworkData data;

                //ordered unique number set, which will be shuffled in each epoch
                Integer[] arr = getOrderedSet(samples.size());

                int i;
                double sample_ratio = Math.floor(samples.size() * network.getTrainPercentage());

                outermost: //for exiting the process
                for (int epoch = 0; epoch < network.getEpochs(); epoch++) {
                    //random set generation
                    Integer[] randomSet = getRandomSet(arr);

                    for (i = 0; i < sample_ratio; i++) {
                        //stop or pause thread
                        synchronized (this) {
                            while (paused) {
                                wait(); // will cause this Thread to block until notify
                            }
                            if(stopped){
                                break outermost;
                            }
                        }
                        NNSample current_sample = samples.get(randomSet[i]); //get new random sample
                        //muscle calculation. the whole software is built around these three mdfcrs.
                        network.calculateOutputs(current_sample);
                        network.updateWeights(current_sample);

                        //construct network data for the view parts
                        data = network.getCurrentNetworkData();
                        publish(data);

                        //adjust speed
                        sleep(delay);
                    }
                    //testing mean squared error with remaining validation samples
                    double ms_error = 0;
                    int j;
                    for (j = i ; j < samples.size(); j++) {
                        network.calculateOutputs(samples.get(randomSet[i]));
                        ms_error += samples.get(j).ms_error;
                    }
                    ms_error = ms_error / (samples.size() - sample_ratio);

                    controller.addErrorGraphValue(ms_error);
                    System.out.print(epoch+1 + ": " + ms_error + "\n");
                }
            } catch (ArithmeticException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        network.setWeights(null);

        return true;
    }

    /**
     * Give data to the view controller to present
     * @param chunks
     */
    @Override
    protected void process(final List<NeuralNetwork.NetworkData> chunks) {
        NeuralNetwork.NetworkData data = chunks.get(chunks.size()-1);
        controller.updateNetworkGraphics(data);
    }

    /*Good to know: executed in the EDT*/
    @Override
    protected void done() {
        try {
            boolean status = get();
            stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}