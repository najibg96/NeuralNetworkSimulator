package neuralnetworksimulator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * <h1>Neural Network Control Panel</h1>
 * An ergonomic User Interface for using the application.
 * All initialization options are available in this panel.
 * The user is able to set everything necessary about the network.
 * The user is able to set layer architecture, initial weights, the sample database file
 * learning rate, learning ratio. During runtime the User is able to pause, and stop.
 * The user is also able to save the architecture to a predefined file.
 * It depends on {@link NNSimulatorViewController}, the controller, and on {@link NeuralNetwork} which is the model.
 *
 * @author Najib
 */
public class NNControlPanel extends JPanel {
    /*Reference to the controller bridge*/
    private NNSimulatorViewController controller;
    /*Reference to the neural network, the model in MVC*/
    private NeuralNetwork network;

    /**
     * Sets the controller object
     * @param controller The controller of the application.
     */
    public void setController(NNSimulatorViewController controller) {
        this.controller = controller;
    }

    /**
     * Sets the network object
     * @param network The neural network model.
     */
    public void setNetwork(NeuralNetwork network) {
        this.network = network;
    }

    ///////////////////////////////////////////////////////////////////////////
    // private fields
    ///////////////////////////////////////////////////////////////////////////
    private javax.swing.JButton LoadArchitectureButton;
    private javax.swing.JButton LoadTrainingDataButton;
    private javax.swing.JButton StartButton;
    private javax.swing.JButton StopButton;
    private javax.swing.JButton ViewButton;
    private javax.swing.JButton ResetViewButton;
    private javax.swing.JButton SaveArchitectureButton;
    private javax.swing.JLabel TotalDataLabel;
    private javax.swing.JSlider DelaySlider;
    private javax.swing.JTextArea TextAreaWeights;
    private javax.swing.JTextField TextFieldLayers;
    private javax.swing.JTextField TextFieldAlfa;
    private javax.swing.JTextField TextFieldPercentage;
    private javax.swing.JTextField TextFieldEpochs;
    private javax.swing.JTextField TextFieldDelay;
    private final JFileChooser fileDialog = new JFileChooser(".");
    private Image starticon;
    private Image pauseicon;

    ///////////////////////////////////////////////////////////////////////////
    // methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Constructor.
     * Creates the visual structure of the control JPanel.
     * The layout was created using the NetBeans layout generator.
     * Uses {@link GroupLayout} to align the components.
     */
    NNControlPanel() {
        TextFieldLayers = new javax.swing.JTextField();
        JLabel labelLayer = new JLabel();
        JLabel labelWeights = new JLabel();
        JScrollPane scrollPaneTextAreaWeights = new JScrollPane();
        TextAreaWeights = new javax.swing.JTextArea();
        LoadArchitectureButton = new javax.swing.JButton();
        JLabel labelAlfa = new JLabel();
        TextFieldAlfa = new javax.swing.JTextField();
        JLabel labelTrainingData = new JLabel();
        LoadTrainingDataButton = new javax.swing.JButton();
        JLabel labelPercentage = new JLabel();
        TextFieldPercentage = new javax.swing.JTextField();
        JLabel labelTraining = new JLabel();
        JLabel labelEpochs = new JLabel();
        TextFieldEpochs = new javax.swing.JTextField();
        TextFieldDelay = new javax.swing.JTextField();

        TotalDataLabel = new JLabel();
        JLabel trainDataLabel = new JLabel();

        StartButton = new javax.swing.JButton();
        StopButton = new javax.swing.JButton();
        ViewButton = new javax.swing.JButton();
        ResetViewButton = new javax.swing.JButton();
        DelaySlider = new javax.swing.JSlider();
        JLabel labelDelay = new JLabel();
        JLabel labelSaveload = new JLabel();
        SaveArchitectureButton = new javax.swing.JButton();

        TotalDataLabel.setText("");
        trainDataLabel.setText("");

        TextFieldLayers.setText("57,2,1"); //inital layer, matching the provided sample database

        labelLayer.setFont(new java.awt.Font("Arial", 1, 14));
        labelLayer.setText("Layers");

        labelWeights.setFont(new java.awt.Font("Arial", 1, 14));
        labelWeights.setText("Weights");

        TextAreaWeights.setColumns(20);
        TextAreaWeights.setRows(5);
        TextAreaWeights.setText("random");
        scrollPaneTextAreaWeights.setViewportView(TextAreaWeights);

        LoadArchitectureButton.setText("Load architecture file");

        labelAlfa.setFont(new java.awt.Font("Arial", 1, 14));
        labelAlfa.setText("Learning rate");

        TextFieldAlfa.setText("0.005");

        labelTrainingData.setFont(new java.awt.Font("Arial", 1, 14));
        labelTrainingData.setText("Training data");

        LoadTrainingDataButton.setText("Load training samples");

        labelPercentage.setText("Train sample percentage per epoch");

        TextFieldPercentage.setText("0.7");

        labelTraining.setFont(new java.awt.Font("Arial", 1, 14));
        labelTraining.setText("Training");

        labelEpochs.setText("Epochs / Cycles");

        TextFieldEpochs.setText("200");

        JLabel labelView = new JLabel("Switch weights/derivatives view");

        starticon = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("resources/play.png"));
        StartButton.setIcon(new ImageIcon(starticon.getScaledInstance(24,24,java.awt.Image.SCALE_SMOOTH)));
        Image stopimg = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("resources/stop.png"));
        StopButton.setIcon(new ImageIcon(stopimg.getScaledInstance(24,24,java.awt.Image.SCALE_SMOOTH)));
        pauseicon = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("resources/pause.png"));

        ViewButton.setText("Weights");
        ResetViewButton.setText("Reset view");

        //delay (speed) of the simulation
        DelaySlider.setMinimum(0);
        DelaySlider.setMaximum(1000);
        DelaySlider.setValue(0);

        labelDelay.setText("Delay [ms]");
        TextFieldDelay.setText(""+DelaySlider.getValue());

        labelSaveload.setFont(new java.awt.Font("Arial", 1, 14));
        labelSaveload.setText("Save/load simulation");

        SaveArchitectureButton.setText("Save architecture");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(this);
        setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPaneTextAreaWeights)
                        .addComponent(TextFieldLayers)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(labelLayer)
                                                .addComponent(labelWeights)
                                                .addComponent(LoadArchitectureButton)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addComponent(LoadTrainingDataButton)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(TotalDataLabel))
                                                .addComponent(labelPercentage)
                                                .addComponent(labelAlfa)
                                                .addComponent(TextFieldAlfa, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addComponent(TextFieldPercentage, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(trainDataLabel))
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addComponent(StartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(StopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(labelEpochs)
                                                                .addComponent(TextFieldEpochs, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(DelaySlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                        .addComponent(labelDelay)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(TextFieldDelay, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(0, 0, Short.MAX_VALUE)))))
                                        .addComponent(labelView)
                                        .addComponent(ViewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ResetViewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labelTrainingData, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labelTraining, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labelSaveload, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(SaveArchitectureButton))
                                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(labelLayer)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TextFieldLayers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelWeights)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollPaneTextAreaWeights, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(LoadArchitectureButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelAlfa)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TextFieldAlfa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelTrainingData)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(LoadTrainingDataButton)
                                        .addComponent(TotalDataLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelPercentage)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(TextFieldPercentage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(trainDataLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelTraining)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelEpochs)
                                        .addComponent(labelDelay)
                                        .addComponent(TextFieldDelay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(DelaySlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(TextFieldEpochs))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(StartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(StopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelView)
                                .addComponent(ViewButton,javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ResetViewButton,javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(labelSaveload)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SaveArchitectureButton)
                                .addContainerGap(75, Short.MAX_VALUE))
        );

        setListeners();
    }

    /**
     * Locks the fields that the User cannot change during simulation.
     */
    public void lockControl() {
        TextFieldLayers.setEnabled(false);
        TextAreaWeights.setEnabled(false);
        LoadArchitectureButton.setEnabled(false);
        TextFieldAlfa.setEnabled(false);
        TextFieldPercentage.setEnabled(false);
        TextFieldEpochs.setEnabled(false);
        LoadTrainingDataButton.setEnabled(false);
    }

    /**
     * Unlocks the fields if they were locked during simulation.
     */
    public void unlockControl(){
        TextFieldLayers.setEnabled(true);
        TextAreaWeights.setEnabled(true);
        LoadArchitectureButton.setEnabled(true);
        TextFieldAlfa.setEnabled(true);
        TextFieldPercentage.setEnabled(true);
        TextFieldEpochs.setEnabled(true);
        LoadTrainingDataButton.setEnabled(true);
    }

    /**
     * Interface for reporting the state of the simulation.
     * Used for changing the start/pause button.
     * @param isRunning
     */
    public void reportNNProcessStatus(boolean isRunning){
        if(isRunning){
            StartButton.setIcon(new ImageIcon(pauseicon.getScaledInstance(24,24,java.awt.Image.SCALE_SMOOTH)));
        } else{
            StartButton.setIcon(new ImageIcon(starticon.getScaledInstance(24,24,java.awt.Image.SCALE_SMOOTH)));
        }
    }

    /**
     * Resets every network option from the input fields.
     */
    public void updateInputs(){
        if(!network.isSetLayers())
            network.setLayers(TextFieldLayers.getText());
        if(!network.isSetWeights())
            network.setWeights(TextAreaWeights.getText());
        network.setLambda_learning(Double.parseDouble(TextFieldAlfa.getText()));
        network.setTrainPercentage(Double.parseDouble(TextFieldPercentage.getText()));
        network.setEpochs(Integer.parseInt(TextFieldEpochs.getText()));
        NNWorker.setDelay(DelaySlider.getValue());
    }

    /**
     * Setting the listeners of the controlpanel fields for responsive operation.
     */
    private void setListeners() {
        /*Layer architecture JTextField input*/
        TextFieldLayers.addActionListener(new AbstractAction() {
            private String prev = "";
            @Override
            public void actionPerformed(ActionEvent e) {
                /*If it has not changed we will not do it again*/
                if(!prev.equals(TextFieldLayers.getText())){
                    network.setLayers(TextFieldLayers.getText());
                    if(network.isSetWeights())
                        controller.updateNetworkGraphics(network.getCurrentNetworkData());
                    LoadArchitectureButton.setText("Load architecture file");
                    prev = TextFieldLayers.getText();
                }
            }
        });
        TextFieldLayers.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }
            private String prev = "";
            @Override
            public void focusLost(FocusEvent e) {
                /*If it has not changed we will not do it again*/
                if(!prev.equals(TextFieldLayers.getText())){
                    network.setLayers(TextFieldLayers.getText());
                    if(network.isSetLayers())
                        controller.setNewNetworkGraphics(network.getLayers());

                    LoadArchitectureButton.setText("Load architecture file");
                    prev = TextFieldLayers.getText();
                }
            }
        });

        /*Weights JTextArea input*/
        TextAreaWeights.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }
            @Override
            public void focusLost(FocusEvent e) {
                network.setWeights(TextAreaWeights.getText());
                if(network.isSetWeights())
                    controller.updateNetworkGraphics(network.getCurrentNetworkData());
                LoadArchitectureButton.setText("Load architecture file");
            }
        });
        TextAreaWeights.addKeyListener(new KeyListener() {
           @Override
           public void keyTyped(KeyEvent e) {

           }

           @Override
           public void keyPressed(KeyEvent e) {
               if (e.getKeyCode() == KeyEvent.VK_TAB) {
                   if (e.getModifiers() > 0) {
                       TextAreaWeights.transferFocusBackward();
                   } else {
                       TextAreaWeights.transferFocus();
                   }
                   e.consume();
               }
           }

           @Override
           public void keyReleased(KeyEvent e) {

           }
        });

        /*Loads architecture from User defined file. File constructed like directed in documentation*/
        LoadArchitectureButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int returnVal = fileDialog.showOpenDialog(NNControlPanel.this);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            LoadArchitectureButton.setText(fileDialog.getSelectedFile().getName());
                            try (BufferedReader br = new BufferedReader(new FileReader(fileDialog.getSelectedFile()))) {
                                String line;
                                line = br.readLine();
                                if (line == null)
                                    return;
                                TextFieldLayers.setText(line);
                                network.setLayers(line); //set layers
                                String content = "";
                                while ((line = br.readLine()) != null) {
                                    content += line + "\n";
                                }
                                TextAreaWeights.setText(content);
                                network.setWeights(content); //set weights
                                if(network.isSetWeights())
                                    controller.updateNetworkGraphics(network.getCurrentNetworkData());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });

        /*Load training data from User defined file. File constructed like directed in documentation */
        LoadTrainingDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileDialog.showOpenDialog(NNControlPanel.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileDialog.getSelectedFile();
                    LoadTrainingDataButton.setText(fileDialog.getSelectedFile().getName());
                    NNWorker.setSampleDB(file);
                    TotalDataLabel.setText(NNWorker.getSamples().size()+" samples");
                }
            }
        });

        /*Reporting the delay slider changes*/
        DelaySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                TextFieldDelay.setText(""+DelaySlider.getValue());
                NNWorker.setDelay(DelaySlider.getValue());
            }
        });
        TextFieldDelay.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int val = Integer.parseInt(TextFieldDelay.getText());
                if(val > DelaySlider.getMaximum())
                    val = DelaySlider.getMaximum();
                if(val < 0)
                    val = 0;
                DelaySlider.setValue(val);
                NNWorker.setDelay(val);
            }
        });
        TextFieldDelay.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                int val = Integer.parseInt(TextFieldDelay.getText());
                if(val > DelaySlider.getMaximum())
                    val = DelaySlider.getMaximum();;
                if(val < 0)
                    val = 0;
                DelaySlider.setValue(val);
                NNWorker.setDelay(val);
            }
        });

        /*Starts the simulation process, then acts and play/pause button. Stimulates lockControl*/
        StartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NNWorker.startToggle();
            }
        });

        /*Stops the simulaton. Stimulates unlockControl*/
        StopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NNWorker.stop();
            }
        });

        /*Switch between the two view mode*/
        ViewButton.addActionListener(new ActionListener() {
            private boolean state = true;
            @Override
            public void actionPerformed(ActionEvent e) {
                state = !state;
                controller.switchWeightDerivativeView(state);
                if(state)
                    ViewButton.setText("Weights");
                else
                    ViewButton.setText("Derivatives");
            }
        });

        ResetViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.resetView();
            }
        });

        /*Calls network architecture saver method*/
        SaveArchitectureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    network.exportArchitecture();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }
}
