package Fractal;
import java.awt.*;

/**
 * Julia Set Fractal
 *
 * The resulting graph is a Julia set fractal.  You can zoom into any
 * rectangular region of the graph by using the mouse.
 */
public class JuliaSetDemo extends DemoFrame
{
    private static final String TITLE = "Julia Set Demo";

    /**
     * Constructor.
     */
    private JuliaSetDemo()
    {
        super(TITLE, new JuliaSetPanel(), 450, 545);
    }

    /**
     * Main.
     */
    public static void main(String args[])
    {
        Frame frame = new JuliaSetDemo();
        frame.setVisible(true);
    }
}
