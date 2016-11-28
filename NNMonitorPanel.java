package neuralnetworksimulator;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * <h1>Monitor panel</h1>
 * Jpanel that helps following the events of the neural network simulation process.
 * This class depends on {@link NNSimulatorViewController} controller object.
 *
 * @author Najib
 */
public class NNMonitorPanel extends JPanel {

    private ColorScale gradientScale; //color scale for the min and max network values
    private JTextArea textArea;  //used as console
    private GraphPanel graphPanel;

    /**
     * Constructor.
     * Instatiates a {@link ColorScale} and a {@link JTextArea} and sets the standard output to it.
     */
    public NNMonitorPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        /*Create text are that will be used as console*/
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setColumns(20);
        textArea.setRows(50);
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
        JScrollPane scrollTextArea = new JScrollPane();
        scrollTextArea.setViewportView(textArea);

        /*Redirecting the stdout to the textArea
        * This will be called from the NNWorker process
        * */
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                updateTextArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };
        System.setOut(new PrintStream(out, true));

        JLabel consoleLabel = new JLabel("Mean squared error per epoch");
        consoleLabel.setForeground(Color.white);
        add(consoleLabel);
        add(scrollTextArea);

        /*Create the colorscale*/
        gradientScale = new ColorScale();
        add(gradientScale);

        JLabel graphLabel = new JLabel("Mean squared error change");
        graphLabel.setForeground(Color.white);
        add(graphLabel);

        /*Create graph of errors*/
        graphPanel = new GraphPanel(new ArrayList<>());
        graphPanel.setPreferredSize(new Dimension(1800, 1600));
        add(graphPanel);

    }

    /**
     * Adds new value to the graph. It may be called from a different thread than EDT.
     * @param value
     */
    public void addGraphValue(double value){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                graphPanel.addScore(value);
                updateUI();
            }
        });
    }

    /**
     * Reset the graph. Delete every value.
     */
    public void resetGraph(){
        graphPanel.setScores(new ArrayList<>());
    }

    public void updateScale(double min, double max){
        gradientScale.updateScale(min, max);
        updateUI();
    }

    /**
     * Appends text to the textArea. It may be called from a different thread than EDT.
     * Used by the TextAreaOutputStream instatiated in the constructor.
     * @param text
     */
    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                textArea.append(text);
            }
        });
    }

}
