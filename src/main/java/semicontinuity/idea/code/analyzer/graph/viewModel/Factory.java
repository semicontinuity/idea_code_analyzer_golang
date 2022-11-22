package semicontinuity.idea.code.analyzer.graph.viewModel;

import java.util.List;

public interface Factory<
        NODE_PAYLOAD,
        COMP,
        IND_COMPS extends COMP,
        NODE extends COMP,
        SPLIT extends COMP,
        LAYER extends COMP
        > {

    IND_COMPS newIndependentComponents(List<? extends COMP> components);

    NODE newNode(NODE_PAYLOAD payload);

    SPLIT newSplit(List<NODE> items, COMP subLayer);

    LAYER newLayer(COMP directDeps, COMP sharedDeps);
}
