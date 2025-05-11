package semicontinuity.idea.code.analyzer.graph.viewModel.ide;

import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory;

public abstract class BaseIdeGraphViewFactory<VERTEX_PAYLOAD> implements Factory<
        VERTEX_PAYLOAD,
        JComponent,
        JComponent,
        JComponent,
        JComponent,
        JComponent
        > {
    static boolean showDebugBorders = false;

    @Override
    public JComponent newIndependentComponents(List<? extends JComponent> components) {
        var box = Box.createVerticalBox();
        if (showDebugBorders) {
            box.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        } else {
            box.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }

        for (JComponent component : components) {
            box.add(component);
        }
        return box;
    }

    @Override
    public JComponent newSplit(List<JComponent> items, JComponent subLayer) {
        var box = Box.createHorizontalBox();
        if (showDebugBorders) {
            box.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
        } else {
            box.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }

        box.add(itemsBox(items));
        box.add(subLayer);
        return box;
    }

    @Override
    public JComponent newLayer(JComponent directDeps, JComponent sharedDeps) {
        var box = Box.createVerticalBox();
        if (showDebugBorders) {
            box.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        } else {
            box.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }

        if (directDeps != null) {
            box.add(directDepsBox(directDeps));
        }
        if (sharedDeps != null) {
            box.add(sharedDepsBox(sharedDeps));
        }
        return box;
    }

    JComponent itemsBox(List<JComponent> items) {
        var box = Box.createVerticalBox();
        if (showDebugBorders) {
            box.setBorder(BorderFactory.createLineBorder(Color.PINK));
        } else {
            box.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }

        for (JComponent component : items) {
            box.add(component);
        }
        return box;
    }

    JComponent directDepsBox(JComponent contents) {
        var box = Box.createVerticalBox();
        if (showDebugBorders) {
            box.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        } else {
            box.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }

        box.add(contents);
        return box;
    }

    JComponent sharedDepsBox(JComponent contents) {
        var box = Box.createVerticalBox();
        if (showDebugBorders) {
            box.setBorder(BorderFactory.createLineBorder(Color.RED));
        } else {
            box.setBorder(BorderFactory.createLoweredBevelBorder());
        }

        box.add(contents);
        return box;
    }
}
