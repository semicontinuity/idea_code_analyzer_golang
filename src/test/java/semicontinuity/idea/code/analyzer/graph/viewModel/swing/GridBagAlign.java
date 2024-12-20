package semicontinuity.idea.code.analyzer.graph.viewModel.swing;

import javax.swing.*;
import java.awt.*;

public class GridBagAlign {
    public static void main(String[] args){
        JFrame mainFrame = new JFrame("Test");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel gridPane = new JPanel();
        gridPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.weighty = 0.0;

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JButton button1 = new JButton("Button 1");
        gridPane.add(button1, gbc);


        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 0;
        JButton button2 = new JButton("Button 2");
        gridPane.add(button2, gbc);


        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JButton button3 = new JButton("Button 3");
        gridPane.add(button3, gbc);


        gbc.gridx = 2;
        gbc.gridy = 1;
        JButton button4 = new JButton("Button 4");
        gridPane.add(button4, gbc);

        mainFrame.add(gridPane);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}