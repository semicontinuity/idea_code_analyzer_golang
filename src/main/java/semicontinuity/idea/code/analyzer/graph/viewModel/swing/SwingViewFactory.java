package semicontinuity.idea.code.analyzer.graph.viewModel.swing;

import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory;

public class SwingViewFactory implements Factory<
        String,
        JComponent,
        JComponent,
        JComponent,
        JComponent,
        JComponent
        > {

    @Override
    public JComponent newNode(String s) {
        System.out.println("  newNode " + s);
        var box = Box.createHorizontalBox();
        box.add(new JButton(s));
        box.add(Box.createHorizontalGlue());
        return box;
    }

    @Override
    public JComponent newIndependentComponents(List<? extends JComponent> components) {
        var box = Box.createVerticalBox();
        box.setForeground(Color.YELLOW);
//        box.setBorder(BorderFactory.createEmptyBorder(4, 1, 4, 1));
        box.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        for (JComponent component : components) {
            box.add(component);
        }
        return box;
    }

    @Override
    public JComponent newSplit(List<JComponent> items, JComponent subLayer) {
        var box = Box.createHorizontalBox();
        box.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
        box.add(itemsBox(items));
        box.add(subLayer);
        return box;
    }

    @Override
    public JComponent newLayer(JComponent directDeps, JComponent sharedDeps) {
        var box = Box.createVerticalBox();
        box.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        if (directDeps != null) box.add(directDepsBox(directDeps));
        if (sharedDeps != null) box.add(sharedDepsBox(sharedDeps));
        return box;
    }

    JComponent itemsBox(List<JComponent> items) {
        var box = Box.createVerticalBox();
        box.setBorder(BorderFactory.createLineBorder(Color.PINK));
        for (JComponent component : items) {
            box.add(component);
        }
        return box;
    }

    JComponent directDepsBox(JComponent contents) {
        var box = Box.createVerticalBox();
//        box.setBackground(Color.GREEN);
        box.setBorder(BorderFactory.createLineBorder(Color.GREEN));
//        box.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        box.add(contents);
        return box;
    }

    JComponent sharedDepsBox(JComponent contents) {
        var box = Box.createVerticalBox();
        box.setBorder(BorderFactory.createLineBorder(Color.RED));
//        box.setBorder(BorderFactory.createLoweredBevelBorder());
        box.add(contents);
        return box;
    }
}
