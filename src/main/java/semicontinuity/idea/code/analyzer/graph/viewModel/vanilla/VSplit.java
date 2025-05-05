package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VSplit<PAYLOAD> extends VComponent {
    @JsonProperty
    private final List<VMember<PAYLOAD>> items;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final VComponent subLayer;

    public VSplit(List<VMember<PAYLOAD>> items, VComponent subLayer) {
        this.items = items;
        this.subLayer = subLayer;
    }
}
