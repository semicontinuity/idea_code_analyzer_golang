package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import com.fasterxml.jackson.annotation.JsonProperty;
import semicontinuity.idea.code.analyzer.graph.viewModel.Node;

public class VNode<PAYLOAD> implements Node {
    @JsonProperty
    private final PAYLOAD payload;

    public VNode(PAYLOAD payload) {
        this.payload = payload;
    }
}
