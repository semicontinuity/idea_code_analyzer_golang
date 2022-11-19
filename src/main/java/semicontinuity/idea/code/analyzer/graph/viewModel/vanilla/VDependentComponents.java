package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VDependentComponents<PAYLOAD> extends VComponent {
    @JsonProperty
    private final List<VNode<PAYLOAD>> front;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final VComponent back;

    public VDependentComponents(List<VNode<PAYLOAD>> front, VComponent back) {
        this.front = front;
        this.back = back;
    }
}
