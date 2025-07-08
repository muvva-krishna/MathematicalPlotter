import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;

/**
 * A zoomable 2D graph panel with centered origin, grid, and coordinate display.
 */
public class GraphPanel extends JPanel implements MouseMotionListener {

    private double scale = 1.0; // scale factor, where 1.0 = 50px per unit
    private final int baseGridSpacing = 50; // base spacing in pixels
    private final DecimalFormat coordinateFormat = new DecimalFormat("0.00");
    private Point mousePosition = null;

    public GraphPanel() {
        setPreferredSize(new Dimension(724, 600));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Graph Area"));
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2d);
        drawAxes(g2d);
        drawMouseCoordinates(g2d);

        g2d.dispose();
    }

    private void drawGrid(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        int originX = width / 2;
        int originY = height / 2;

        int spacing = (int) (baseGridSpacing * scale);

        g2d.setColor(new Color(220, 220, 220));

        // Vertical grid lines
        for (int x = originX; x < width; x += spacing)
            g2d.drawLine(x, 0, x, height);
        for (int x = originX; x > 0; x -= spacing)
            g2d.drawLine(x, 0, x, height);

        // Horizontal grid lines
        for (int y = originY; y < height; y += spacing)
            g2d.drawLine(0, y, width, y);
        for (int y = originY; y > 0; y -= spacing)
            g2d.drawLine(0, y, width, y);
    }

    private void drawAxes(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        int originX = width / 2;
        int originY = height / 2;

        g2d.setColor(Color.BLACK);
        g2d.drawLine(0, originY, width, originY); // X-axis
        g2d.drawLine(originX, 0, originX, height); // Y-axis

        // Tick marks and labels
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g2d.setColor(Color.BLACK);

        int spacing = (int) (baseGridSpacing * scale);

        // X-axis ticks
        for (int x = originX + spacing, unit = 1; x < width; x += spacing, unit++) {
            g2d.drawLine(x, originY - 4, x, originY + 4);
            g2d.drawString(String.valueOf(unit), x - 5, originY + 15);
        }
        for (int x = originX - spacing, unit = -1; x > 0; x -= spacing, unit--) {
            g2d.drawLine(x, originY - 4, x, originY + 4);
            g2d.drawString(String.valueOf(unit), x - 10, originY + 15);
        }

        // Y-axis ticks
        for (int y = originY - spacing, unit = 1; y > 0; y -= spacing, unit++) {
            g2d.drawLine(originX - 4, y, originX + 4, y);
            g2d.drawString(String.valueOf(unit), originX + 6, y + 4);
        }
        for (int y = originY + spacing, unit = -1; y < height; y += spacing, unit--) {
            g2d.drawLine(originX - 4, y, originX + 4, y);
            g2d.drawString(String.valueOf(unit), originX + 6, y + 4);
        }
    }

    private void drawMouseCoordinates(Graphics2D g2d) {
        if (mousePosition == null) return;

        int width = getWidth();
        int height = getHeight();
        int originX = width / 2;
        int originY = height / 2;

        double graphX = (mousePosition.x - originX) / (baseGridSpacing * scale);
        double graphY = -(mousePosition.y - originY) / (baseGridSpacing * scale);

        String label = "(" + coordinateFormat.format(graphX) + ", " + coordinateFormat.format(graphY) + ")";

        g2d.setColor(Color.BLUE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2d.drawString(label, mousePosition.x + 10, mousePosition.y - 10);

        // Crosshair
        g2d.setColor(new Color(0, 0, 255, 50));
        g2d.drawLine(mousePosition.x, 0, mousePosition.x, height);
        g2d.drawLine(0, mousePosition.y, width, mousePosition.y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition = e.getPoint();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // not used
    }

    public void zoomIn() {
        scale *= 1.2;
        repaint();
    }

    public void zoomOut() {
        scale /= 1.2;
        repaint();
    }

    public void resetZoom() {
        scale = 1.0;
        repaint();
    }
}
