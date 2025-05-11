package semicontinuity.idea.code.analyzer.graph.viewModel.ide;

import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import semicontinuity.idea.code.analyzer.golang.Member;

public class MembersGraphViewFactory extends BaseIdeGraphViewFactory<Member> {
    static ImageIcon methodIcon = icon("/icons/method.png");

    private final IdeButtonHighlightingDispatcher ideButtonHighlightingDispatcher;

    public MembersGraphViewFactory(IdeButtonHighlightingDispatcher ideButtonHighlightingDispatcher) {
        this.ideButtonHighlightingDispatcher = ideButtonHighlightingDispatcher;
    }

    @Override
    public JComponent newVertex(Member vertex) {
        var box = Box.createHorizontalBox();
        var ideButton = new IdeButton(
                vertex.getPsiElement(),
                vertex.getName(),
                methodIcon,
                vertex,
                ideButtonHighlightingDispatcher
        );
        ideButtonHighlightingDispatcher.register(vertex, ideButton);

        box.add(ideButton);
        box.add(Box.createHorizontalGlue());
        box.add(Box.createHorizontalGlue());
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
