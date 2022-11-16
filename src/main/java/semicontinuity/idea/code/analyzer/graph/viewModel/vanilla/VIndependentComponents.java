package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VIndependentComponents extends VComponent {

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<? extends VComponent> components;

    public VIndependentComponents(List<? extends VComponent> components) {
        this.components = components;
    }
}
