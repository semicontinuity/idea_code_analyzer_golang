package semicontinuity.idea.code.analyzer.graph.viewModel.ide;

import java.util.HashMap;
import java.util.function.Consumer;

import semicontinuity.idea.code.analyzer.golang.Node;
import semicontinuity.idea.code.analyzer.graph.DAGraph;

public class IdeButtonHighlightingDispatcher implements Consumer<Node> {
    private final HashMap<Node, IdeButton> mapping = new HashMap<>();
    private final DAGraph<Node> callGraph;

    public IdeButtonHighlightingDispatcher(DAGraph<Node> callGraph) {
        this.callGraph = callGraph;
    }

    public void register(Node node, IdeButton button) {
        mapping.put(node, button);
    }

    @Override
    public void accept(Node node) {
        deselectAll();
        mapping.get(node).select(NodeHighlightingKind.SUBJECT);

        callGraph.forEachUpstreamNode(node, caller -> mapping.get(caller).select(NodeHighlightingKind.CALLER));
        callGraph.forEachDownstreamNode(node, callee -> mapping.get(callee).select(NodeHighlightingKind.CALLEE));
    }

    public void deselectAll() {
        mapping.values().forEach(IdeButton::deselect);
    }
}
