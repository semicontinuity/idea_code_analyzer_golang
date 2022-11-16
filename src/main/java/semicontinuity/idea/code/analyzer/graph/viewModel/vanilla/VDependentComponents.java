package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VDependentComponents<PAYLOAD> extends VComponent {
    @JsonProperty
    private final List<VNode<PAYLOAD>> heads;
    @JsonProperty
    private final VComponent deeperLayers;

    public VDependentComponents(List<VNode<PAYLOAD>> heads, VComponent deeperLayers) {
        this.heads = heads;
        this.deeperLayers = deeperLayers;
    }
}
