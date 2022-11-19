package semicontinuity.idea.code.analyzer.graph.viewModel;

import java.util.List;

public interface Factory<
        NODE_PAYLOAD,
        COMP,
        IND_COMPS extends COMP,
        FANOUT extends COMP,
        DEP_COMPS extends COMP,
        NODE extends COMP,
        SPLIT extends COMP,
        LAYER extends COMP
        > {

    IND_COMPS newIndependentComponents(List<? extends COMP> components);

    FANOUT newFanout(NODE head, COMP deeperLayers);

    DEP_COMPS newDependentComponents(List<NODE> heads, COMP deeperLayers);

    NODE newNode(NODE_PAYLOAD payload);

    SPLIT newSplit(List<NODE> items, COMP subLayer);

    LAYER newLayer(COMP directDeps, COMP sharedDeps);
}
