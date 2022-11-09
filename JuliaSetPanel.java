package Fractal;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class JuliaSetPanel extends GraphPanel
{
    private static final String PRESET_NAMES[] = {
        "",
        "Claws", "Snowflakes", "Turtles",
        "Serpents", "Amoebas", "Sparklers"
    };

    private static final double PRESET_REALS[] = {
        0,
        0.30900264, 0.33843684, -0.3985014,
        -0.8184639, -0.3346212, -0.5530404
    };

    private static final double PRESET_IMAGINARIES[] = {
        0,
        -0.0339787, -0.4211402, 0.5848901,
        -0.2129812, 0.6340579, 0.5933997
    };

    /** control panel */    private Panel fractalControlPanel = new Panel();
    /** bounds panel */     private Panel boundsPanel         = new Panel();

    /** real label */       private Label realLabel      = new Label("Real:");
    /** imaginary label */  private Label imaginaryLabel = new Label("Imaginary:");

    /** real text */        private TextField realText      = new TextField();
    /** imaginary text */   private TextField imaginaryText = new TextField();

    /** x-minimum label */  private Label xMinLabel = new Label("Min x:");
    /** x-maximum label */  private Label xMaxLabel = new Label("Max x:");
    /** y-minimum label */  private Label yMinLabel = new Label("Min y:");
    /** y-maximum label */  private Label yMaxLabel = new Label("Max y:");

    /** x-minimum text */   private Label xMinText  = new Label();
    /** x-maximum text */   private Label xMaxText  = new Label();
    /** y-minimum text */   private Label yMinText  = new Label();
    /** y-maximum text */   private Label yMaxText  = new Label();

    /** preset choices */   private Choice presets = new Choice();

    /** manual button */    private Button manualButton = new Button("Manual");
    /** random button */    private Button randomButton = new Button("Random");

    /** filler 1 */         private Label filler1 = new Label();
    /** filler 2 */         private Label filler2 = new Label();
    /** filler 3 */         private Label filler3 = new Label();
    /** filler 4 */         private Label filler4 = new Label();

    /** iteration counter */    private int        n          = 0;
    /** fractal plot thread */  private PlotThread plotThread = null;

    /** panel width */          private int   w;
    /** panel height */         private int   h;
    /** real value */           private double real;
    /** imaginary value */      private double imaginary;
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

    /** complex value c */          private Complex c;
    /** true to stop run thread */  private boolean stopFlag = false;

    /** initial plot properties */
    private static PlotProperties plotProps =
                                        new PlotProperties(0, 0, 0, 0);

    /** random number generator */
    Random random = new Random(System.currentTimeMillis());

    /**
     * Constructor.
     */
    JuliaSetPanel()
    {
        super("Julia Set of z^2 + c", plotProps, false);

        Font labelFont = getLabelFont();
        Font textFont  = getTextFont();

        // Fractal controls.
        realLabel.setFont(labelFont);
        realLabel.setAlignment(Label.RIGHT);
        imaginaryLabel.setFont(labelFont);
        imaginaryLabel.setAlignment(Label.RIGHT);
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
        fractalControlPanel.setLayout(new GridLayout(0, 6, 2, 2));
        fractalControlPanel.add(realLabel);
        fractalControlPanel.add(realText);
        fractalControlPanel.add(xMinLabel);
        fractalControlPanel.add(xMinText);
        fractalControlPanel.add(yMinLabel);
        fractalControlPanel.add(yMinText);
        fractalControlPanel.add(imaginaryLabel);
        fractalControlPanel.add(imaginaryText);
        fractalControlPanel.add(xMaxLabel);
        fractalControlPanel.add(xMaxText);
        fractalControlPanel.add(yMaxLabel);
        fractalControlPanel.add(yMaxText);
        fractalControlPanel.add(filler1);
        fractalControlPanel.add(presets);
        fractalControlPanel.add(filler2);
        fractalControlPanel.add(filler3);
        fractalControlPanel.add(manualButton);
        fractalControlPanel.add(randomButton);
        addDemoControls(fractalControlPanel);

        // Load the preset choice names.
        for (int i = 0; i < PRESET_NAMES.length; ++i) {
            presets.addItem(PRESET_NAMES[i]);
        }

        // Preset choice handler.
        presets.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev)
            {
                int index = presets.getSelectedIndex();

                realText.setText(Double.toString(PRESET_REALS[index]));
                imaginaryText.setText(Double.toString(PRESET_IMAGINARIES[index]));

                startPlot();
            }
        });

        // Manual button handler.
        manualButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {
                presets.select(0);
                startPlot();
            }
        });

        // Random button handler.
        randomButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {
                double r1 = 2*random.nextDouble() - 1;
                double r2 = 2*random.nextDouble() - 1;

                realText.setText(Double.toString(r1));
                imaginaryText.setText(Double.toString(r2));

                presets.select(0);
                startPlot();
            }
        });
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
     * Process the real and imaginary text fields.
     * @throws Exception if a field is invalid
     */
    private void processTextFields() throws Exception
    {
        if ((realText.getText().trim().length()      == 0) ||
            (imaginaryText.getText().trim().length() == 0))
        {
            throw new Exception("You must enter values for the " +
                                "real and imaginary parts.");
        }

        try {
            real = Double.valueOf(realText.getText());
        }
        catch(NumberFormatException ex) {
            throw new Exception("Invalid real part: " +
                                realText.getText());
        }

        try {
            imaginary = Double.valueOf(imaginaryText.getText());
        }
        catch(NumberFormatException ex) {
            throw new Exception("Invalid imaginary part: " +
                                imaginaryText.getText());
        }

        c = new Complex(real, imaginary);
    }

    /**
     * Start the plot.
     */
    private void startPlot()
    {
        try {
            processTextFields();
        }
        catch(Exception ex) {
            processUserError(ex.getMessage());
            return;
        }

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

        if ((realText.getText().trim().length()      == 0) ||
            (imaginaryText.getText().trim().length() == 0)) return;

        stopPlot();

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
                    Complex z  = new Complex(x0, y0);  // z = x0 + y0i

                    /**
                     * IMPLEMENT YOUR OWN CODE HERE, instead of the following example.
                     *
                     * Example: color each pixel represented by the complex number z
                     * according to the norm of the complex number. 
                     *
                     * If the distance to 0 is lesser than 2, color the pixel.
                     * The color of those pixel is related to the distance of
                     * its complex number to 0. Far pixels are darker than close pixels.
                     *
                     * Hint: The complex parameter c of the Julia Fractal is already given
                     *       as an instance variable (see line 76), just use it. The update 
                     *       of c through the GUI is also already implemented (see line 252).
                     */

                    int iterations = 0;
                    while (z.abs() < ESCAPE_LIMIT && iterations < MAX_ITERS) {
                        z = z.mul(z).add(JuliaSetPanel.this.c);
                        iterations++;
                    }

                    double radius = 2.0;

                    // decide of which color should be the pixel at the 
                    // coordinates (col,row), represented by the complex number c
                    if ((z.abs() < radius) && (iterations < MAX_ITERS)){
                        // calculate a color value between 0 and 255 according to
                        // a property of the complex number
                        int k = 255 - (int)((255*z.abs())/radius);
                        //k = Math.min(k, 240);

                        // color the pixel with the coordinates (col,row)
                        // with the rgb-color (k,k,k)
                        // for more colorful visualisations, you need to
                        // determine three color values, one for each r,g,b
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

