package Fractal;
import java.awt.*;

/**
 * Mandelbrot Set Fractal
 *
 * Graph the Mandelbrot set fractal.  You can zoom into any
 * rectangular region of the graph by using the mouse.
 */
public class MandelbrotSetDemo extends DemoFrame
{
    private static final String TITLE = "Mandelbrot Set Demo";

    /**
     * Constructor.
     */
    private MandelbrotSetDemo()
    {
        super(TITLE, new MandelbrotSetPanel(), 450, 525);
    }

    /**
     * Main.
     */
    public static void main(String args[])
    {
        Frame frame = new MandelbrotSetDemo();
        frame.setVisible(true);
    }
}
