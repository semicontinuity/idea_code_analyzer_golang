package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VLayer extends VComponent {
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final VComponent directDeps;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final VComponent sharedDeps;

    public VLayer(VComponent directDeps, VComponent sharedDeps) {
        this.directDeps = directDeps;
        this.sharedDeps = sharedDeps;
    }
}
