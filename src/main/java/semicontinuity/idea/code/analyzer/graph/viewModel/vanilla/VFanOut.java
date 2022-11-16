package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import semicontinuity.idea.code.analyzer.graph.viewModel.FanOut;

public class VFanOut<PAYLOAD> implements FanOut {
    @JsonProperty
    private final VNode<PAYLOAD> head;
    @JsonProperty
    private final List<VComponent> followers;

    public VFanOut(VNode<PAYLOAD> head, List<VComponent> followers) {
        this.head = head;
        this.followers = followers;
    }
}
