package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VNode<PAYLOAD> extends VComponent {
    @JsonProperty
    private final PAYLOAD payload;

    public VNode(PAYLOAD payload) {
        this.payload = payload;
    }
}
