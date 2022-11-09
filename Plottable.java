package Fractal;
import java.awt.Rectangle;

public interface Plottable
{
    /**
     * Get the root function's image rectangle
     * @return the rectangle
     */
    Rectangle getRectangle();

    /**
     * Get the root function's properties
     * @return the properties
     */
    PlotProperties getPlotProperties();

    /**
     * Return the value of the function at x.
     * @return the function value
     */
    double at(double x);
}
