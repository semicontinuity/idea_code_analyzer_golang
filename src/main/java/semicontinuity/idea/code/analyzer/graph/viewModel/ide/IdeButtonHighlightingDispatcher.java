package semicontinuity.idea.code.analyzer.graph.viewModel.ide;

import java.util.function.Consumer;

import semicontinuity.idea.code.analyzer.golang.Node;

public class IdeButtonHighlightingDispatcher implements Consumer<Node> {
    @Override
    public void accept(Node node) {
        deselectAll();
    }

    public void deselectAll() {
    }
}
