package neuralnetworksimulator;

import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.*;

/**
 * <h1>Graphics panel</h1>
 * Jpanel that draws and animates an externally constructed neural network.
 * This class uses the {@link neuralnetworksimulator.NeuralNetwork.Layer} class
 * and depends on {@link NNSimulatorViewController} controller object.
 *
 * @author Najib Ghadri
 */
public class NNGraphicsPanel extends JPanel {

    private double tx, ty, ltx, lty; //translation (moving), centered to left-top
    private double s=1; //scale, initially ratio of 1
    private Point mousePt;

    /*Shapes and colors*/
    private Map<Shape, Point> circles = new HashMap<>(); //circles and their neuron index
    private Map<Shape, Color> circlesColor = new HashMap<>(); //circles and their color

    private Map<Shape, Point3D> lines = new HashMap<>(); //lines and their neuron-input index
    private Map<Shape, Color> linesColor = new HashMap<>(); //lines and their colors

// TODO: 17/11/2016 Tags can go into lines map
//    private List<TextShape> tags = new ArrayList<>();
//
//    private class TextShape {
//        String text;
//        int x, y;
//        Color clr;
//
//        public TextShape(String text, int x, int y, Color clr) {
//            this.text = text;
//            this.x = x;
//            this.y = y;
//            this.clr = clr;
//        }
//    }

    /**
     * Constructor.
     * Initializes listeneres.
     */
    public NNGraphicsPanel() {

        /*Moving (dragging) the canvas after the cursor*/
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                tx = e.getX() - mousePt.x + ltx;
                ty = e.getY() - mousePt.y + lty;
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        /*Pressing and clicking listeners*/
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mousePt = e.getPoint();
                // TODO: 17/11/2016 Selection system: neuron and its verts
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
                ltx = tx;
                lty = ty;
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });

        /*Zooming with the wheel*/
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double actx = (e.getX());
                double acty = (e.getY());

                double z = 1;
                if(e.getWheelRotation() < 0){
                    if(s < 10)
                        z =1.1;
                } else{
                    if(s > 0.2)
                        z = 1/1.1;
                }

                tx -= (actx-tx)-(actx-tx)/z;
                ty -= (acty-ty)-(acty-ty)/z;
                s*=z;

                repaint();
            }
        });
    }

    /**
     * Resets the scale (zoom) and translation (move) fields to initial values.
     */
    public void resetView(){
        s = 1;
        tx = ty = ltx = lty = 0;
        updateUI();
    }

    /**
     * Constructs the shapes of the network in parameter. The network is divided into
     * layers consisted of neurons that have connection between other neurons of other layers.
     *
     * @param layers - new network architecture
     */
    public void setNewNetwork(ArrayList<NeuralNetwork.Layer> layers){
        circles = new HashMap<>();
        circlesColor = new HashMap<>();

        lines = new HashMap<>();
        linesColor = new HashMap<>();

        int i, j, k;
        int max = 0; //tallest layer
        for(i=0; i < layers.size(); i++){
            if(layers.get(i).y.getDimension() >  max)
                max = layers.get(i).y.getDimension();
        }

        int vgap = 60; //vertical gap between two neurons
        int HEIGHT = max * vgap; //height of the network
        int hgap = HEIGHT / layers.size()+30; //horizontal gap between two layers
        int WIDTH = layers.size() * hgap; //width of the network
        int r = 40;
        int ofs = 100;

        /*Constructing the visual network shapes*/
        for(i=0; i < layers.size(); i++){
            /*vertical offset of the current layer to symmetrize the network*/
            int vertoffs = (HEIGHT -(layers.get(i).y.getDimension()* vgap))/2;
            for(j = 0; j < layers.get(i).y.getDimension(); j++){
                if(i>0){
                    /*vertical offset of the next layer, to draw lines correctly connected*/
                    int nextvertoffs = (HEIGHT -(layers.get(i-1).y.getDimension()* vgap))/2;
                    for(k =0; k < layers.get(i-1).y.getDimension(); k++){
                        /*Resulting line coordinates*/
                        Shape shape = new Line2D.Double(ofs+((i-1)* hgap),ofs+nextvertoffs+(k* vgap),ofs+(i* hgap),ofs+vertoffs+(j* vgap));
                        linesColor.put(shape,Color.BLACK);
                        lines.put(shape,new Point3D(i,j,k));
                    }
                }
                /*Neuron moved down with the offset, padded with hgap and vgap*/
                Shape shape = new Ellipse2D.Double(ofs+(i* hgap)-(r /2),ofs+vertoffs+(j* vgap)-(r /2), r, r);
                circlesColor.put(shape,Color.WHITE);
                circles.put(shape,new Point(i,j));
            }
        }
        resetView();
        updateUI(); //calls paintComponent
    }

    /**
     * Draws the network with color scaled lines based on the derivatives of the weights.
     * @param layers - changed network
     */
    public void updateDerivativeLayers(final ArrayList<NeuralNetwork.Layer> layers, double min, double max){
        //tags = new ArrayList<>();

        /*Update the colors of the lines based on the values*/
        for(Shape shape : lines.keySet()){
            Point3D co = lines.get(shape);
            Line2D line = (Line2D) shape;
            double w = layers.get((int)co.getX()).d_W.getEntry((int)co.getY(),(int)co.getZ());
            double n = ((w-min)/(max-min));
            n = n*(120.0/360.0);
            linesColor.put(line,Color.getHSBColor((float)n,1.0f,0.95f));
            //tags.add(new TextShape(Double.toString(w),(int)(line.getX1()+line.getX2())/2,(int)(line.getY1()+line.getY2())/2,Color.BLACK));
        }

        updateUI(); //calls paintComponent
    }

    /**
     * Draws the network with color scaled lines based on the weights.
     * @param layers - changed network
     */
    public void updateWeightLayers(final ArrayList<NeuralNetwork.Layer> layers, double min, double max){
        //tags = new ArrayList<>();

        /*Update the colors of the lines based on the values*/
        for(Shape shape : lines.keySet()){
            Point3D co = lines.get(shape);
            Line2D line = (Line2D) shape;
            double w = layers.get((int)co.getX()).W.getEntry((int)co.getY(),(int)co.getZ());
            double n = ((w-min)/(max-min));
            n = n*(120.0/360.0);
            linesColor.put(line,Color.getHSBColor((float)n,1.0f,0.95f));
            //tags.add(new TextShape(Double.toString(w),(int)(line.getX1()+line.getX2())/2,(int)(line.getY1()+line.getY2())/2,Color.BLACK));
        }

        updateUI(); //calls paintComponent
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(tx,ty);
        g2.scale(s, s);

        /*draw lines under neurons*/
        for (Shape shape : lines.keySet()) {
            Color color = linesColor.get(shape);
            g2.setColor(color);
            g2.draw(shape);
        }

        /*draw neurons*/
        for (Shape shape : circles.keySet()) {
            Color color = circlesColor.get(shape);
            g2.setColor(color);
            g2.fill(shape);
            g2.setColor(Color.BLACK);
            g2.draw(shape);
        }

//        for (TextShape shape : tags) {
//            g2.drawString(shape.text,shape.x,shape.y);
//        }
    }
}
