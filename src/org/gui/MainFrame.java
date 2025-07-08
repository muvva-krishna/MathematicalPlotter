import javax.swing.*;
import java.awt.*;
/**
 * Main application window that contains the menu bar, graph panel, and sidebar.
 */
public class MainFrame extends JFrame {

    private JMenuBar mainMenuBar;
    private GraphPanel graphPanel;
    private SidePanel sidePanel;

    public MainFrame() {
        setTitle("Mathematical Function Plotter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 600));
        initializeComponents();
        setupLayout();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeComponents() {
        // Menu bar setup
        mainMenuBar = new JMenuBar();
        setJMenuBar(mainMenuBar);

        JMenu fileMenu = new JMenu("File");
        mainMenuBar.add(fileMenu);

        JMenu viewMenu = new JMenu("View");
        mainMenuBar.add(viewMenu);

        // Panels
        graphPanel = new GraphPanel();
        sidePanel = new SidePanel();
    }

    private void setupLayout() {
        // Side panel on the right, graph panel on the left
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT, graphPanel, sidePanel
        );
        splitPane.setDividerLocation(724); // Adjust to leave ~300px for sidebar
        splitPane.setContinuousLayout(true);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(splitPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
