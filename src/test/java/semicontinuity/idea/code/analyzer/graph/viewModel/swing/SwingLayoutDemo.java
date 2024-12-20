package semicontinuity.idea.code.analyzer.graph.viewModel.swing;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SwingLayoutDemo {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;

    public static void main(String[] args) {
        new SwingLayoutDemo().showGridBagLayoutDemo();
    }

    public SwingLayoutDemo() {
        prepareGUI();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Java SWING Examples");
        mainFrame.setSize(400, 400);
        mainFrame.setLayout(new GridLayout(3, 1));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        headerLabel = new JLabel("", JLabel.CENTER);

        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setSize(350, 100);

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
    }

    private void showGridBagLayoutDemo() {
        headerLabel.setText("Layout in action: GridBagLayout");

        JPanel panel = new JPanel();
//        panel.setBackground(Color.darkGray);
        panel.setSize(300, 300);
        GridBagLayout layout = new GridBagLayout();

        panel.setLayout(layout);

        panel.add(new JButton("Participant 1"), new GridBagConstraints() {{
            fill = GridBagConstraints.BOTH;
            gridx = 0;
            gridwidth = 3;
            gridy = 0;
//            insets = new Insets(4, 4, 4, 4);
        }});

        panel.add(new JButton("Participant 2"), new GridBagConstraints() {{
            fill = GridBagConstraints.BOTH;
            gridx = 3;
            gridwidth = 3;
            gridy = 0;
//            insets = new Insets(4, 4, 4, 4);
        }});



        // =========================================================================

        panel.add(new JComponent() {}, new GridBagConstraints() {{
            fill = GridBagConstraints.BOTH;
            gridx = 0;
            gridy = 1;
//            weightx = 1;
            ipadx = 16;
        }});
        panel.add(new JButton("p1"), new GridBagConstraints() {{
            fill = GridBagConstraints.BOTH;
            gridx = 1;
            gridy = 1;
        }});

        // =========================================================================

        panel.add(
                new JComponent() {},
                new GridBagConstraints() {{
                    fill = GridBagConstraints.BOTH;
                    gridx = 2;
                    gridwidth = 2;
                    gridy = 1;
//                    weightx = 8;
                }}
        );

        // =========================================================================

        panel.add(new JButton("p2"), new GridBagConstraints() {{
            fill = GridBagConstraints.BOTH;
            gridx = 4;
            gridy = 1;
        }});
        panel.add(new JComponent() {}, new GridBagConstraints() {{
            fill = GridBagConstraints.BOTH;
            gridx = 5;
//            gridwidth = GridBagConstraints.REMAINDER;
            gridy = 1;
//            weightx = 4;
        }});

        // =========================================================================

        controlPanel.add(panel);
        mainFrame.setVisible(true);
    }
}