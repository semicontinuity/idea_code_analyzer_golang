package semicontinuity.idea.code.analyzer.graph.viewModel.ide;

import java.awt.Color;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import semicontinuity.idea.code.analyzer.golang.Node;
import semicontinuity.idea.code.analyzer.graph.viewModel.Factory;

public class IdeViewFactory implements Factory<
        Node,
        JComponent,
        JComponent,
        JComponent,
        JComponent,
        JComponent
        > {

    static ImageIcon methodIcon = icon("/nodes/method.png");

    private final IdeButtonHighlightingDispatcher ideButtonHighlightingDispatcher;

    public IdeViewFactory(IdeButtonHighlightingDispatcher ideButtonHighlightingDispatcher) {
        this.ideButtonHighlightingDispatcher = ideButtonHighlightingDispatcher;
    }


    @Override
    public JComponent newNode(Node node) {
        var box = Box.createHorizontalBox();
        var ideButton = new IdeButton(node.getPsiElement(), node.getName(), methodIcon, node, ideButtonHighlightingDispatcher);
        box.add(ideButton);
        box.add(Box.createHorizontalGlue());
        box.add(Box.createHorizontalGlue());
        ideButtonHighlightingDispatcher.register(node, ideButton);
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

    protected static ImageIcon icon(final String iconResource) {
        final URL resource = IdeButton.class.getResource(iconResource);
        if (resource == null) {
            return null;
        }
        return new ImageIcon(resource);
    }
}
