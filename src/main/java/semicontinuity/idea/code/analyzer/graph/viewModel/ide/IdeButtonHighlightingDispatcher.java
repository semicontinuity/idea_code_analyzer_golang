package semicontinuity.idea.code.analyzer.graph.viewModel.ide;

import java.util.HashMap;
import java.util.function.Consumer;

import semicontinuity.idea.code.analyzer.golang.Node;

public class IdeButtonHighlightingDispatcher implements Consumer<Node> {
    private final HashMap<Node, IdeButton> mapping = new HashMap<>();

    public void register(Node node, IdeButton button) {
        mapping.put(node, button);
    }

    @Override
    public void accept(Node node) {
        deselectAll();
        mapping.get(node).select(NodeHighlightingKind.SUBJECT);
    }

    public void deselectAll() {
        mapping.values().forEach(IdeButton::deselect);
    }
}
