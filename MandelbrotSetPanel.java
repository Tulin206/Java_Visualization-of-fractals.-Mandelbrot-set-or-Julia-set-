package Fractal;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class MandelbrotSetPanel extends GraphPanel
{
    /** control panel */    private Panel fractalControlPanel = new Panel();

    /** x-minimum label */  private Label xMinLabel = new Label("Min x:");
    /** x-maximum label */  private Label xMaxLabel = new Label("Max x:");
    /** y-minimum label */  private Label yMinLabel = new Label("Min y:");
    /** y-maximum label */  private Label yMaxLabel = new Label("Max y:");

    /** x-minimum text */   private Label xMinText  = new Label();
    /** x-maximum text */   private Label xMaxText  = new Label();
    /** y-minimum text */   private Label yMinText  = new Label();
    /** y-maximum text */   private Label yMaxText  = new Label();

    /** iteration counter */    private int        n          = 0;
    /** fractal plot thread */  private PlotThread plotThread = null;

    /** panel width */          private int   w;
    /** panel height */         private int   h;
    /** minimum x value */      private double xMin;
    /** maximum x value */      private double xMax;
    /** minimum y value */      private double yMin;
    /** maximum y value */      private double yMax;
    /** delta per pixel */      private double delta;
    /** previous minimum x */   private double oldXMin;
    /** previous maximun y */   private double oldYMax;

    /** zoom rectangle upper left row */        private int r1;
    /** zoom rectangle upper left column */     private int c1;
    /** zoom rectangle bottom right row */      private int r2;
    /** zoom rectangle bottom right column */   private int c2;

    /** true to stop run thread */  private boolean stopFlag = false;

    /** initial plot properties */
    private static PlotProperties plotProps =
                                        new PlotProperties(0, 0, 0, 0);

    /**
     * Constructor.
     */
    MandelbrotSetPanel()
    {
        super("Mandelbrot Set", plotProps, false);

        Font labelFont = getLabelFont();
        Font textFont  = getTextFont();

        // Fractal controls.
        xMinLabel.setFont(labelFont);
        xMinLabel.setAlignment(Label.RIGHT);
        xMinText.setFont(textFont);
        xMaxLabel.setFont(labelFont);
        xMaxLabel.setAlignment(Label.RIGHT);
        xMaxText.setFont(textFont);
        yMinLabel.setFont(labelFont);
        yMinLabel.setAlignment(Label.RIGHT);
        yMinText.setFont(textFont);
        yMaxLabel.setFont(labelFont);
        yMaxLabel.setAlignment(Label.RIGHT);
        yMaxText.setFont(textFont);

        // Fractal control panel.
        fractalControlPanel.setBackground(Color.lightGray);
        fractalControlPanel.setLayout(new GridLayout(0, 4, 2, 2));
        fractalControlPanel.add(xMinLabel);
        fractalControlPanel.add(xMinText);
        fractalControlPanel.add(yMinLabel);
        fractalControlPanel.add(yMinText);
        fractalControlPanel.add(xMaxLabel);
        fractalControlPanel.add(xMaxText);
        fractalControlPanel.add(yMaxLabel);
        fractalControlPanel.add(yMaxText);
        addDemoControls(fractalControlPanel);
    }

    /**
     * Get the plot properties.
     */
    private void getProperties()
    {
        w     = plotProps.getWidth();
        h     = plotProps.getHeight();
        delta = plotProps.getXDelta();
        xMin  = plotProps.getXMin();
        xMax  = plotProps.getXMax();
        yMin  = yMax - h*delta;
        yMax  = plotProps.getYMax();
    }

    /**
     * Set the plot properties.
     */
    private void setProperties()
    {
        w     = plotProps.getWidth();
        h     = plotProps.getHeight();
        delta = 0.01f;
        xMin  = -(w/2)*delta;
        yMin  = -(h/2)*delta;
        xMax  = -xMin;
        yMax  = -yMin;

        plotProps.update(xMin, xMax, yMin, yMax);
    }

    /**
     * Display the current plot bounds in the text controls.
     */
    private void displayBounds()
    {
        xMinText.setText(Double.toString(xMin));
        xMaxText.setText(Double.toString(xMax));
        yMinText.setText(Double.toString(yMin));
        yMaxText.setText(Double.toString(yMax));
    }

    /**
     * Start the plot.
     */
    private void startPlot()
    {
        stopPlot();     // stop currently running plot

        n = 0;
        draw();
    }

    //------------------//
    // Method overrides //
    //------------------//

    /**
     * Plot a function.
     */
    protected void plotFunction()
    {
        // First time or not?
        if (n == 0) {
            setProperties();
        }
        else {
            getProperties();
        }
        displayBounds();

        // Start a new plot thread.
        stopFlag   = false;
        plotThread = new PlotThread();
        plotThread.start();

        ++n;
    }

    /**
     * Stop the plot.
     */
    private void stopPlot()
    {
        if ((plotThread != null) && (plotThread.isAlive())) {
            stopFlag = true;

            try {
                plotThread.join();
            }
            catch(Exception ex) {}
        }
    }

    /**
     * Mouse pressed on the plot:  Start the zoom rectangle.
     */
    public void mousePressedOnPlot(MouseEvent ev)
    {
        if ((plotThread != null) && (plotThread.isAlive())) return;

        oldXMin = xMin;
        oldYMax = yMax;

        // Starting corner.
        c1 = ev.getX();
        r1 = ev.getY();

        // Ending corner.
        c2 = -1;
        r2 = -1;

        setXORMode();
    }

    /**
     * Mouse dragged on the plot:  Track the mouse to draw the zoom rectangle.
     */
    public void mouseDraggedOnPlot(MouseEvent ev)
    {
        if ((plotThread != null) && (plotThread.isAlive())) return;

        // Erase the previous rectangle.
        if ((c2 != -1) && (r2 != -1)) {
            plotRectangle(Math.min(c1, c2),  Math.min(r1, r2),
                          Math.abs(c1 - c2), Math.abs(r1 - r2),
                          Color.black);
        }

        // Current ending corner.
        c2 = ev.getX();
        r2 = ev.getY();

        // Calculate and display new zoom area bounds.
        xMin = oldXMin + delta*Math.min(c1, c2);
        xMax = oldXMin + delta*Math.max(c1, c2);
        yMin = oldYMax - delta*Math.max(r1, r2);
        yMax = oldYMax - delta*Math.min(r1, r2);
        displayBounds();

        // Draw the new rectangle.
        plotRectangle(Math.min(c1, c2),  Math.min(r1, r2),
                      Math.abs(c1 - c2), Math.abs(r1 - r2),
                      Color.black);
    }

    /**
     * Mouse released on the plot:  End the zoom rectangle and
     * plot the zoomed area.
     */
    public void mouseReleasedOnPlot(MouseEvent ev)
    {
        if ((plotThread != null) && (plotThread.isAlive())) return;

        // Draw the rectangle.
        if ((c2 != -1) && (r2 != -1)) {
            plotRectangle(Math.min(c1, c2),  Math.min(r1, r2),
                          Math.abs(c1 - c2), Math.abs(r1 - r2),
                          Color.black);
        }

        // Plot the area in the rectangle.
        plotProps.update(xMin, xMax, yMin, yMax);
        draw();
    }

    //-----------//
    // Animation //
    //-----------//

    private static final int    MAX_ITERS    = 32;
    private static final double ESCAPE_LIMIT = 2.0;

    /**
     * Graph thread class that varies over
     * each point in the complex plane bounded by the rectangle
     * xMin, xMax, yMin, yMax.
     */
    private class PlotThread extends Thread
    {
        public void run()
        {
            // Loop over each graph panel pixel.
            for (int row = 0; row < h; ++row) {
                double y0 = yMax - row*delta;          // row => y0

                for (int col = 0; col < w; ++col) {
                    double  x0 = xMin + col*delta;     // col => x0

                    // termination of the Thread if requested
                    if (stopFlag) return;

                    // this is the current complex number with the coordinates
                    // (col, row) in the window
                    Complex c  = new Complex(x0, y0);  // c = x0 + y0i

                    /**
                     * IMPLEMENT YOUR OWN CODE HERE, instead of the following example.
                     *
                     * Example: color each pixel represented by the complex number c
                     * according to the norm of the complex number. 
                     *
                     * If the distance to 0 is lesser than 2, color the pixel.
                     * The color of those pixel is related to the distance of
                     * its complex number to 0. Far pixels are darker than close pixels.
                     */
                    
                    double x = 0, y = 0;
                    int iterations = 0;
                    while (x*x+y*y < ESCAPE_LIMIT && iterations < MAX_ITERS) {
                        double x_new = x*x-y*y+x0;
                        y = 2*x*y+y0;
                        x = x_new;
                        iterations++;
                    } c = new Complex (x,y);
                    // if (iterations < MAX_ITERS) image.setRGB(col, row, white);
                    // else image.setRGB(col, row, black);

                    double radius = 2.0;

                    // decide of which color should be the pixel at the 
                    // coordinates (col,row), represented by the complex number c
                    if ((c.abs() < radius) && (iterations < MAX_ITERS)){
                
                        // calculate a color value between 0 and 255 according to
                        // a property of the complex number
                        int k = 255 - (int)((255*c.abs())/radius);
                        //k = Math.min(k, 240);

                        // color the pixel with the coordinates (col,row)
                        // with the rgb-color (k,k,k)
                        plotPoint(col, row, new Color(k, k, k));
                    }
                }

                // Draw a row of the graph.
                yield();
                drawPlot();
            }
        }
    }
}

