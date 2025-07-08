import javax.swing.*;
import java.awt.*;

/**
 * A basic 2D graph panel using Java2D.
 */
public class GraphPanel extends JPanel {

    public GraphPanel() {
        setPreferredSize(new Dimension(724, 600));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Graph Area"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawAxes((Graphics2D) g);
    }

    private void drawAxes(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();

        // Anti-aliasing for smooth lines
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.GRAY);

        // X-axis
        g2d.drawLine(0, height / 2, width, height / 2);
        // Y-axis
        g2d.drawLine(width / 2, 0, width / 2, height);

        // Optional: Draw tick marks or grid
    }
}
