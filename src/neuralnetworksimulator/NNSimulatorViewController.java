package neuralnetworksimulator;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

/**
 * Controls and structures the application by an MVC approach.
 * Establishes communication between {@link NeuralNetwork} - as model and
 * {@link NNControlPanel}, {@link NNGraphicsPanel} and {@link NNMonitorPanel}-as view
 * and itself as the controller.
 */
public class NNSimulatorViewController extends JPanel {
    private NeuralNetwork network;
    private NNControlPanel controlPanel;
    private NNGraphicsPanel graphicsPanel;
    private NNMonitorPanel monitorPanel;
    private NNWorker nnWorker;

    /**
     *  <h1>Neural Network Controller</h1>
     * Constructs the network, controlpanel, grahicspanel and monitorpanel.
     *
     * @author Najib
     */
    public NNSimulatorViewController() {
        super();



        //View controller has all objects
        network = new NeuralNetwork();
        controlPanel = new NNControlPanel();

        //Give controller bridge to model executor and controller in MVC
        controlPanel.setController(this);
        NNWorker.setController(this);

        //Give model to the "real" controller in MVC
        controlPanel.setNetwork(network);
        NNWorker.setNetwork(network);

        /*Create view/presentation*/
        graphicsPanel = new NNGraphicsPanel();
        monitorPanel = new NNMonitorPanel();
        initGUIComponents();
    }

    /**
     * Construct application window structure
     */
    private void initGUIComponents() {
        JSplitPane jSplitPane = new JSplitPane();
        JScrollPane jScrollControlPanel = new JScrollPane();
        JScrollPane jScrollMonitorPanel = new JScrollPane();

        jScrollControlPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollControlPanel.setMaximumSize(null);
        jScrollControlPanel.setName("");
        jScrollControlPanel.add(controlPanel);
        jScrollControlPanel.setViewportView(controlPanel);

//        jScrollMonitorPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        jScrollMonitorPanel.setMaximumSize(null);
//        jScrollMonitorPanel.setName("");
//        jScrollMonitorPanel.add(monitorPanel);
//        jScrollMonitorPanel.setViewportView(monitorPanel);

        JSplitPane jSplitPane2 = new JSplitPane();
        jSplitPane2.setDividerLocation(800);
        jSplitPane2.setLeftComponent(graphicsPanel);
        jSplitPane2.setRightComponent(monitorPanel);
        jSplitPane2.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                jSplitPane2.setDividerLocation(jSplitPane2.getWidth()-320);
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        }); //to make monitorPanle fixed width

        jSplitPane.setDividerLocation(300);
        jSplitPane.setLeftComponent(jScrollControlPanel);
        jSplitPane.setRightComponent(jSplitPane2);

        javax.swing.GroupLayout parentPanelLayout = new javax.swing.GroupLayout(this);
        setLayout(parentPanelLayout);
        parentPanelLayout.setHorizontalGroup(
                parentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1112, Short.MAX_VALUE)
        );
        parentPanelLayout.setVerticalGroup(
                parentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSplitPane)
        );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Control graphicsPanel and monitorPanel view
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Add new value to error graph in monitorPanel
     * @param value - new error value
     */
    public void addErrorGraphValue(double value){
        monitorPanel.addGraphValue(value);
    }

    /**
     * Reset the error graph in monitorPanel.
     */
    public void resetErrorGraph(){
        monitorPanel.resetGraph();
    }

    /**
     * View update of the network using {@link NNGraphicsPanel} and {@link NeuralNetwork.Layer}
     * @param data
     */
    public void updateNetworkGraphics(NeuralNetwork.NetworkData data){
        if(viewSwitch){
            graphicsPanel.updateWeightLayers(data.layers,data.minw,data.maxw);
            monitorPanel.updateScale(data.minw,data.maxw);
        }else{
            graphicsPanel.updateDerivativeLayers(data.layers,data.mindw,data.maxdw);
            monitorPanel.updateScale(data.mindw,data.maxdw);
        }
    }

    /**
     * View initialization of network graphics using {@link NNGraphicsPanel} and {@link NeuralNetwork.Layer}
     * @param layers
     */
    public void setNewNetworkGraphics(ArrayList<NeuralNetwork.Layer> layers){
        graphicsPanel.setNewNetwork(layers);
    }

    /*Switch between derivative and weight view*/
    private boolean viewSwitch = true;

    /**
     * Switch between weight and derivative view
     * @param state
     */
    public void switchWeightDerivativeView(boolean state){
        viewSwitch = state;
    }

    /**
     * Reset the view to original state using {@link NNGraphicsPanel}
     */
    public void resetView(){
        graphicsPanel.resetView();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Control controlPanel view
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Lock or unlock {@link NNControlPanel} controlpanel
     * @param state
     */
    public void lockControl(boolean state){
        if(state)
            controlPanel.lockControl();
        else
            controlPanel.unlockControl();
    }

    /**
     * Update the User inputs using {@link NNControlPanel}
     */
    public void updateUserInputs(){
        controlPanel.updateInputs();
    }

    /**
     * Report the process state to {@link NNControlPanel}
     * @param state
     */
    public void reportNNWorkerStatus(boolean state){
        controlPanel.reportNNProcessStatus(state);
    }

}
