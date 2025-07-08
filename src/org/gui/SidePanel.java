import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Sidebar panel to input equations and dynamically add more inputs.
 * Each input field is labeled as "Expression 1", "Expression 2", etc.
 */
public class SidePanel extends JPanel {

    private JPanel equationListPanel;
    private JScrollPane scrollPane;
    private int equationCount = 0;

    public SidePanel() {
        setPreferredSize(new Dimension(300, 800));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Equations"));

        // Panel to hold equations vertically
        equationListPanel = new JPanel();
        equationListPanel.setLayout(new BoxLayout(equationListPanel, BoxLayout.Y_AXIS));

        // Scrollable pane for equation list
        scrollPane = new JScrollPane(equationListPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Add initial two equations
        addEquationField();
        addEquationField();

        // Add button
        JButton addButton = new JButton("Add Equation");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEquationField();
                revalidate();
                repaint();
            }
        });

        // Add scroll and button to panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(addButton);
        bottomPanel.add(Box.createVerticalStrut(10));

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Adds a new labeled equation input field (Expression N).
     */
    private void addEquationField() {
        equationCount++;

        JPanel singleEquationPanel = new JPanel();
        singleEquationPanel.setLayout(new BoxLayout(singleEquationPanel, BoxLayout.Y_AXIS));
        singleEquationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel("Expression " + equationCount + ":");
        JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        singleEquationPanel.add(label);
        singleEquationPanel.add(textField);
        singleEquationPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        equationListPanel.add(singleEquationPanel);
    }
}
