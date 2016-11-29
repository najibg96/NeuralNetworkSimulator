package neuralnetworksimulator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

// TODO: 17/11/2016 Neuron select, index, bias, input output value, sum value, PAUSE and Step
// TODO: 16/11/2016 Process percentage, learning inputs, testing inputs counter
// TODO: 16/11/2016 Control panel validation
// TODO: 16/11/2016 Graph of error
// TODO: 16/11/2016 JUnit testing
// TODO: 16/11/2016 Usecasek, Diagramm, szekvencia, diagrammok


/**
 * <h1>Neural network simulator on swing GUI</h1>
 * Main class that instantiates {@link NNSimulatorViewController}. Runs the application as the JFrame.
 *
 * @author Najib Ghadri
 * @version last
 * @since 2016.10.
 */
public class NNSimulatorApplication extends JFrame {
    private NNSimulatorViewController simulator;

    /**
     * Constructor of the application JFrame.
     * Initializes visual aspects of the fram and instatiates an {@link NNSimulatorViewController}.
     *
     * @see NNSimulatorViewController
     */
    public NNSimulatorApplication() {
        super("Neural network simulator..");
        setPreferredSize(new Dimension(1500,860));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        simulator = new NNSimulatorViewController();
        add(simulator);
        try {
            /*This is not necessary for proper operation, rather foran unforgettable experience :)*/
            setIconImage(ImageIO.read(new File("src/resources/epe.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pack();
    }

    /**
     * Runs the application.
     * @param args
     */
    public static void main(String[] args) {
        UIManager.put("control", new Color(70,73,75)); // Color theme of the swing window

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        /* Run on EDT */
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NNSimulatorApplication().setVisible(true);
            }
        });
    }
}
