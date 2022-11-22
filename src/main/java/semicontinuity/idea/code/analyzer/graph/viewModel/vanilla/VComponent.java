package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "kind")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = VIndependentComponents.class, name = "independent"),
                @JsonSubTypes.Type(value = VNode.class, name = "node"),
                @JsonSubTypes.Type(value = VSplit.class, name = "split"),
                @JsonSubTypes.Type(value = VLayer.class, name = "layer"),
        }
)
public abstract class VComponent {
}
