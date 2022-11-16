package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import semicontinuity.idea.code.analyzer.graph.viewModel.Component;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "kind")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = VDependentComponents.class, name = "dependent"),
                @JsonSubTypes.Type(value = VFanOut.class, name = "fanout"),
                @JsonSubTypes.Type(value = VIndependentComponents.class, name = "independent"),
                @JsonSubTypes.Type(value = VNode.class, name = "node")
        }
)
public abstract class VComponent implements Component {
}
