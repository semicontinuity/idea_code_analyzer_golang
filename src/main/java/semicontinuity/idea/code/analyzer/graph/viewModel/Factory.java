package semicontinuity.idea.code.analyzer.graph.viewModel;

import java.util.List;

public interface Factory<
        NODE_PAYLOAD,
        COMP extends Component,
        IND_COMPS extends IndependentComponents,
        FANOUT extends FanOut,
        DEP_COMPS extends DependentComponents,
        NODE extends Node
        > {

    IND_COMPS newIndependentComponents(List<COMP> components);

    FANOUT newFanout(NODE head, List<COMP> followers);

    DEP_COMPS newDependentComponents(List<NODE> heads, COMP deeperLayers);

    NODE newNode(NODE_PAYLOAD payload);
}
