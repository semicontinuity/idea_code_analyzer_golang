package semicontinuity.idea.code.analyzer.graph.viewModel.swing;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class JFrameExample {
    public static void main(String[] args) {
        var f = new JFrame("test");

        var p = new JPanel();
        p.setLayout(new FlowLayout());

        var b = new JButton("press");
        p.add(b);

        f.add(p);

//        f.setSize(200, 300);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
