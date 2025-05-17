package semicontinuity.idea.code.analyzer.graph.viewModel;

import java.util.List;

public interface Factory<
        VERTEX_PAYLOAD,
        COMP,
        IND_COMPS extends COMP,
        VERTEX extends COMP,
        SPLIT extends COMP,
        LAYER extends COMP
        > {

    IND_COMPS newIndependentComponents(List<? extends COMP> components);

    VERTEX newVertex(VERTEX_PAYLOAD payload);

    SPLIT newSplit(List<VERTEX> items, COMP subLayer);

    LAYER newLayer(COMP directDeps, COMP sharedDeps);
}
