package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import semicontinuity.idea.code.analyzer.graph.viewModel.IndependentComponents;

public class VIndependentComponents implements IndependentComponents {

    @JsonProperty
    private final List<VComponent> components;

    public VIndependentComponents(List<VComponent> components) {
        this.components = components;
    }
}
