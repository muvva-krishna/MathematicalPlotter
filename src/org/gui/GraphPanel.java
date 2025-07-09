import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.*;

/**
 * A zoomable and pannable 2D graph panel with centered origin, grid, coordinate display,
 * and static point marking on mouse click.
 */
public class GraphPanel extends JPanel implements MouseMotionListener, MouseWheelListener, MouseListener {

    private double scale = 1.0; // scale factor, where 1.0 = 50px per unit
    private final int baseGridSpacing = 50;
    private final DecimalFormat coordinateFormat = new DecimalFormat("0.00");
    private Point mousePosition = null;

    private int offsetX = 0;
    private int offsetY = 0;
    private Point lastDragPoint = null;

    private final java.util.List<Point2D.Double> markedPoints = new ArrayList<>();

    public GraphPanel() {
        setPreferredSize(new Dimension(724, 600));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Graph Area"));
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2d);
        drawAxes(g2d);
        drawMarkedPoints(g2d);
        drawMouseCoordinates(g2d);

        g2d.dispose();
    }

    private void drawGrid(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        int originX = width / 2 + offsetX;
        int originY = height / 2 + offsetY;

        int spacing = (int) (baseGridSpacing * scale);
        g2d.setColor(new Color(220, 220, 220));

        for (int x = originX; x < width; x += spacing)
            g2d.drawLine(x, 0, x, height);
        for (int x = originX; x > 0; x -= spacing)
            g2d.drawLine(x, 0, x, height);

        for (int y = originY; y < height; y += spacing)
            g2d.drawLine(0, y, width, y);
        for (int y = originY; y > 0; y -= spacing)
            g2d.drawLine(0, y, width, y);
    }

    private void drawAxes(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        int originX = width / 2 + offsetX;
        int originY = height / 2 + offsetY;

        g2d.setColor(Color.BLACK);
        g2d.drawLine(0, originY, width, originY);
        g2d.drawLine(originX, 0, originX, height);

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g2d.setColor(Color.BLACK);
        int spacing = (int) (baseGridSpacing * scale);

        for (int x = originX + spacing, unit = 1; x < width; x += spacing, unit++) {
            g2d.drawLine(x, originY - 4, x, originY + 4);
            g2d.drawString(String.valueOf(unit), x - 5, originY + 15);
        }
        for (int x = originX - spacing, unit = -1; x > 0; x -= spacing, unit--) {
            g2d.drawLine(x, originY - 4, x, originY + 4);
            g2d.drawString(String.valueOf(unit), x - 10, originY + 15);
        }

        for (int y = originY - spacing, unit = 1; y > 0; y -= spacing, unit++) {
            g2d.drawLine(originX - 4, y, originX + 4, y);
            g2d.drawString(String.valueOf(unit), originX + 6, y + 4);
        }
        for (int y = originY + spacing, unit = -1; y < height; y += spacing, unit--) {
            g2d.drawLine(originX - 4, y, originX + 4, y);
            g2d.drawString(String.valueOf(unit), originX + 6, y + 4);
        }
    }

    private void drawMarkedPoints(Graphics2D g2d) {
        int originX = getWidth() / 2 + offsetX;
        int originY = getHeight() / 2 + offsetY;

        for (Point2D.Double point : markedPoints) {
            int screenX = (int) (originX + point.x * baseGridSpacing * scale);
            int screenY = (int) (originY - point.y * baseGridSpacing * scale);

            g2d.setColor(Color.RED);
            g2d.fillOval(screenX - 4, screenY - 4, 8, 8);

            g2d.setColor(Color.BLACK);
            String label = "(" + coordinateFormat.format(point.x) + ", " + coordinateFormat.format(point.y) + ")";
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2d.drawString(label, screenX + 6, screenY - 6);
        }
    }

    private void drawMouseCoordinates(Graphics2D g2d) {
        if (mousePosition == null) return;

        int width = getWidth();
        int height = getHeight();
        int originX = width / 2 + offsetX;
        int originY = height / 2 + offsetY;

        double graphX = (mousePosition.x - originX) / (baseGridSpacing * scale);
        double graphY = -(mousePosition.y - originY) / (baseGridSpacing * scale);

        String label = "(" + coordinateFormat.format(graphX) + ", " + coordinateFormat.format(graphY) + ")";

        g2d.setColor(Color.BLUE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2d.drawString(label, mousePosition.x + 10, mousePosition.y - 10);

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
        if (SwingUtilities.isRightMouseButton(e) && lastDragPoint != null) {
            int dx = e.getX() - lastDragPoint.x;
            int dy = e.getY() - lastDragPoint.y;
            offsetX += dx;
            offsetY += dy;
            lastDragPoint = e.getPoint();
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            lastDragPoint = e.getPoint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastDragPoint = null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            int originX = getWidth() / 2 + offsetX;
            int originY = getHeight() / 2 + offsetY;

            double graphX = (e.getX() - originX) / (baseGridSpacing * scale);
            double graphY = -(e.getY() - originY) / (baseGridSpacing * scale);

            markedPoints.add(new Point2D.Double(graphX, graphY));
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double zoomFactor = 1.1;
        int notches = e.getWheelRotation();
        Point mouse = e.getPoint();

        double oldScale = scale;

        if (notches < 0) {
            scale *= zoomFactor;
        } else {
            scale /= zoomFactor;
        }

        scale = Math.max(0.05, Math.min(scale, 100));

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        double graphX = (mouse.x - centerX - offsetX) / (baseGridSpacing * oldScale);
        double graphY = -(mouse.y - centerY - offsetY) / (baseGridSpacing * oldScale);

        int newPixelX = (int) (graphX * baseGridSpacing * scale);
        int newPixelY = (int) (-graphY * baseGridSpacing * scale);

        offsetX = mouse.x - centerX - newPixelX;
        offsetY = mouse.y - centerY - newPixelY;

        repaint();
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
        offsetX = 0;
        offsetY = 0;
        repaint();
    }
}
