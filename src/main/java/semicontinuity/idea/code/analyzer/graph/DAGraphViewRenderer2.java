package semicontinuity.idea.code.analyzer.graph;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory;

public class DAGraphViewRenderer2<N,
        NODE_PAYLOAD,
        COMP,
        IND_COMPS extends COMP,
        FANOUT extends COMP,
        DEP_COMPS extends COMP,
        NODE extends COMP> {

    private final Factory<NODE_PAYLOAD, COMP, IND_COMPS, FANOUT, DEP_COMPS, NODE> viewFactory;
    private final Function<N, NODE_PAYLOAD> payloadFunction;

    public DAGraphViewRenderer2(
            Factory<NODE_PAYLOAD, COMP, IND_COMPS, FANOUT, DEP_COMPS, NODE> viewFactory,
            Function<N, NODE_PAYLOAD> payloadFunction) {
        this.viewFactory = viewFactory;
        this.payloadFunction = payloadFunction;
    }


    public COMP render(DAGraph<N> graph) {
        if (!graph.hasEdges()) {
            var nodeViews = graph.nodes()
                    .stream()
                    .map(node -> viewFactory.newNode(payloadFunction.apply(node)))
                    .collect(Collectors.toList());
            return viewFactory.newIndependentComponents(nodeViews);
        }

        var decompose = graph.decompose();
        var components = decompose.entrySet()
                .stream()
                .map(this::renderIndependentComponent)
                .collect(Collectors.toList());

        if (components.size() == 1) {
            return components.get(0);
        } else {
            return viewFactory.newIndependentComponents(components);
        }
    }


    COMP renderIndependentComponent(Map.Entry<Set<N>, DAGraph<N>> rootsWithSubgraph) {
        var roots = rootsWithSubgraph.getKey();
        var subGraph = rootsWithSubgraph.getValue();
        var subGraphView = render(subGraph);

        var rootsViews = roots.stream()
                .map(r -> viewFactory.newNode(payloadFunction.apply(r)))
                .collect(Collectors.toList());

        if (rootsViews.size() == 1) {
            return viewFactory.newFanout(rootsViews.get(0), subGraphView);
        } else {
            return viewFactory.newDependentComponents(rootsViews, subGraphView);
        }
    }
}
