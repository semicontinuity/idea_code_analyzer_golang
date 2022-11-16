package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VFanOut<PAYLOAD> extends VComponent {
    @JsonProperty
    private final VNode<PAYLOAD> head;
    @JsonProperty
    private final VComponent followers;

    public VFanOut(VNode<PAYLOAD> head, VComponent followers) {
        this.head = head;
        this.followers = followers;
    }
}
