package Fractal;
import java.awt.*;
import java.awt.event.*;

/**
 * The panel that contains the x-min, x-max, y-min, and y-max text controls
 * for the function plot bounds.
 */
class PlotBoundsPanel extends Panel
{
    /** x-min
        label */      private Label xMinLabel = new Label("Min x:");
    /** y-min
        label */      private Label xMaxLabel = new Label("Max x:");
    /** x-max
        label */      private Label yMinLabel = new Label("Min y:");
    /** y-max
        label */      private Label yMaxLabel = new Label("Max y:");
    /** x-min
        textfield */  private BoundsTextField xMinText = new BoundsTextField();
    /** x-max
        textfield */  private BoundsTextField xMaxText = new BoundsTextField();
    /** y-min
        textfield */  private BoundsTextField yMinText = new BoundsTextField();
    /** y-max
        textfield */  private BoundsTextField yMaxText = new BoundsTextField();

    /** label font */ private Font labelFont = new Font("Dialog", 1, 12);

    /** parent graph panel */   GraphPanel graphPanel;

    /**
     * Constructor.
     * @param graphPanel the parent graph panel
     */
    PlotBoundsPanel(GraphPanel graphPanel)
    {
        this.graphPanel = graphPanel;

        // Bounds controls.
        xMinLabel.setFont(labelFont);
        xMinLabel.setAlignment(Label.RIGHT);
        xMaxLabel.setFont(labelFont);
        xMaxLabel.setAlignment(Label.RIGHT);
        yMinLabel.setFont(labelFont);
        yMinLabel.setAlignment(Label.RIGHT);
        yMaxLabel.setFont(labelFont);
        yMaxLabel.setAlignment(Label.RIGHT);

        // Bounds panel.
        setBackground(Color.lightGray);
        setLayout(new GridLayout(0, 4, 5, 2));
        add(xMinLabel);
        add(xMinText);
        add(yMinLabel);
        add(yMinText);
        add(xMaxLabel);
        add(xMaxText);
        add(yMaxLabel);
        add(yMaxText);
    }

    /**
     * Set the text fields from the plot properties.
     * @param plotProps the plot properties
     */
    void setTextFields(PlotProperties plotProps)
    {
        xMinText.setText(Double.toString(plotProps.getXMin()));
        xMaxText.setText(Double.toString(plotProps.getXMax()));
        yMinText.setText(Double.toString(plotProps.getYMin()));
        yMaxText.setText(Double.toString(plotProps.getYMax()));
    }

    /**
     * Update the plot properties from the text fields.
     * @param plotProps the plot properties
     */
    void updatePlotProperties(PlotProperties plotProps)
    {
        if (!boundsOK()) return;

        plotProps.update(Double.valueOf(xMinText.getText()).floatValue(),
                         Double.valueOf(xMaxText.getText()).floatValue(),
                         Double.valueOf(yMinText.getText()).floatValue(),
                         Double.valueOf(yMaxText.getText()).floatValue());
    }

    /**
     * Check the bounds.
     * @return true if bounds are OK, else return false
     */
    private boolean boundsOK()
    {
        BoundsTextField fields[] = {xMinText, xMaxText, yMinText, yMaxText};
        double          values[] = new double[4];

        for (int i = 0; i < 4; ++i) {
            try {
                values[i] = Double.valueOf(fields[i].getText()).doubleValue();
                fields[i].setForeground(Color.black);
            }
            catch(Exception ex) {
                fields[i].setForeground(Color.red);
                graphPanel.processUserError("Invalid number format.");
                return false;
            }
        }

        if (values[1] > values[0]) {
            fields[1].setForeground(Color.black);
        }
        else {
            fields[1].setForeground(Color.red);
            graphPanel.processUserError("Min x > Max x");
            return false;
        }

        if (values[3] > values[2]) {
            fields[3].setForeground(Color.black);
        }
        else {
            fields[3].setForeground(Color.red);
            graphPanel.processUserError("Min y > Max y");
            return false;
        }

        return true;
    }

    /**
     * The bounds text field class.
     */
    private class BoundsTextField extends TextField
    {
        /**
         * Constructor.
         */
        private BoundsTextField()
        {
            super(3);

            // Action (return key) handler.
            addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    if (boundsOK()) {
                        graphPanel.plotBoundsChanged();     // callback
                    }
                }
            });

            // Focus (tab key) handler.
            addFocusListener(new FocusAdapter()
            {
                public void focusLost(FocusEvent ev)
                {
                    if (boundsOK()) {
                        graphPanel.plotBoundsChanged();     // callback
                    }
                }
            });
        }
    }
}
