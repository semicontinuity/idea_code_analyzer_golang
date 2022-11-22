package semicontinuity.idea.code.analyzer.graph.viewModel.swing;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class JFrameExample {
    public static void main(String[] args) {
        frame(vbox());
    }

    private static void frame(JComponent contents) {
        var f = new JFrame("test");
        f.setContentPane(contents);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private static Box vbox() {
        var box = Box.createVerticalBox();
        box.add(hbox1());
        box.add(hbox2());
        return box;
    }

    private static JComponent hbox1() {
        var box = Box.createHorizontalBox();
        box.add(new JButton("h1 press 1"));
        box.add(new JButton("h1 press 2"));
        return box;
    }

    private static JComponent hbox2() {
        var box = Box.createHorizontalBox();
        box.add(new JButton("h2 press 1"));
        box.add(new JButton("h2 press 2"));
        return box;
    }
}
