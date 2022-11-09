package Fractal;
import java.awt.*;

/**
 * The properties of a function plot.
 */
public class PlotProperties
{
    /** width */                private int   w;
    /** height */               private int   h;
    /** x-axis row */           private int   xAxisRow;
    /** y-axis column */        private int   yAxisCol;
    /** minimum x value */      private double xMin;
    /** maximum x value */      private double xMax;
    /** minimum y value */      private double yMin;
    /** maximum y value */      private double yMax;
    /** x delta per pixel */    private double xDelta;
    /** y delta per pixel */    private double yDelta;

    /**
     * Constructor.
     * @param xMin the minimum x value
     * @param xMax the maximum x value
     * @param yMin the minimum y value
     * @param yMax the maximum y value
     */
    public PlotProperties(double xMin, double xMax, double yMin, double yMax)
    {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    /**
     * Return the minimum x value.
     * @return the minimum x value
     */
    public double getXMin() { return xMin; }

    /**
     * Return the maximum x value.
     * @return the maximum x value
     */
    public double getXMax() { return xMax; }

    /**
     * Return the minimum y value.
     * @return the minimum y value
     */
    public double getYMin() { return yMin; }

    /**
     * Return the maximum y value.
     * @return the maximum y value
     */
    public double getYMax() { return yMax; }

    /**
     * Return the x delta value.
     * @return the x delta value
     */
    public double getXDelta() { return xDelta; }

    /**
     * Return the y delta value.
     * @return the y delta value
     */
    public double getYDelta() { return yDelta; }

    /**
     * Return the width.
     * @return the width
     */
    public int getWidth() { return w; }

    /**
     * Return the height.
     * @return the height
     */
    public int getHeight() { return h; }

    /**
     * Return the x-axis row.
     * @return the row
     */
    public int getXAxisRow() { return xAxisRow; }

    /**
     * Return the y-axis column.
     * @return the column
     */
    public int getYAxisColumn() { return yAxisCol; }

    /**
     * Update the bounds values.
     * @param xMin the minimum x value
     * @param xMax the maximum x value
     * @param yMin the minimum y value
     * @param yMax the maximum y value
     */
    public void update(double xMin, double xMax, double yMin, double yMax)
    {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    /**
     * Compute property values.
     * @param plotSize the plot panel size
     */
    void compute(Dimension plotSize)
    {
        w = plotSize.width;
        h = plotSize.height;

        xDelta = (xMax - xMin)/w;
        yDelta = (yMax - yMin)/h;

        xAxisRow = (int) Math.round( yMax/yDelta);
        yAxisCol = (int) Math.round(-xMin/xDelta);
    }
}
