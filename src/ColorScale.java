package neuralnetworksimulator;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Simple gradient colorscale jpanel for showing min and max values by color.
 * Currently it uses green-yellow-red colors and 3 point values.
 *
 * @author Najib
 */
public class ColorScale extends JPanel {
    private double min, max;
    public ColorScale() {
        setPreferredSize(new Dimension(300, 800));
    }

    /**
     * Draws the gradient box with its values under it.
     * @param g
     */
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

            /*300 pixels wide box.*/
        Rectangle r1 = new Rectangle(0,5,150,50);
        Rectangle r2 = new Rectangle(150,5,150,50);

        g2.setPaint(new GradientPaint(0, 0, Color.green, 150, 0, Color.yellow, false));
        g2.fill(r1);
        g2.setPaint(new GradientPaint(150, 0, Color.yellow, 300, 0, Color.red, false));
        g2.fill(r2);

            /*minimum maximum and middle value*/
        g2.setColor(Color.white);
        g2.drawString(new DecimalFormat("#.#####").format(max),10,70);
        g2.drawString(new DecimalFormat("#.#####").format((max+min)/2),140,70);
        g2.drawString(new DecimalFormat("#.#####").format(min),250,70);
    }

    /**
     * Updates the values of the colorscale
     * @param min
     * @param max
     */
    public void updateScale(double min, double max){
        this.min = min;
        this.max = max;
        updateUI();
    }
}
