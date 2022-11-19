package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VFanOut<PAYLOAD> extends VComponent {
    @JsonProperty
    private final VNode<PAYLOAD> front;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final VComponent back;

    public VFanOut(VNode<PAYLOAD> front, VComponent back) {
        this.front = front;
        this.back = back;
    }
}
