package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VMember<PAYLOAD> extends VComponent {
    @JsonProperty
    private final PAYLOAD payload;

    public VMember(PAYLOAD payload) {
        this.payload = payload;
    }
}
