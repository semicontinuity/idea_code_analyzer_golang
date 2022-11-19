package semicontinuity.idea.code.analyzer.graph;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory;

public class DAGraphViewRenderer<N,
        NODE_PAYLOAD,
        COMP,
        IND_COMPS extends COMP,
        FANOUT extends COMP,
        DEP_COMPS extends COMP,
        NODE extends COMP> {

    private final Factory<NODE_PAYLOAD, COMP, IND_COMPS, FANOUT, DEP_COMPS, NODE> viewFactory;
    private final Function<N, NODE_PAYLOAD> payloadFunction;

    public DAGraphViewRenderer(
            Factory<NODE_PAYLOAD, COMP, IND_COMPS, FANOUT, DEP_COMPS, NODE> viewFactory,
            Function<N, NODE_PAYLOAD> payloadFunction) {
        this.viewFactory = viewFactory;
        this.payloadFunction = payloadFunction;
    }


    public COMP render(DAGraph<N> graph) {
        var view = doRender(graph);
        if (view == null) return viewFactory.newIndependentComponents(List.of());
        return view;
    }

    private COMP doRender(DAGraph<N> graph) {
        if (!graph.hasNodes()) return null;

        if (!graph.hasEdges()) {
            var nodeViews = graph.nodes()
                    .stream()
                    .map(node -> viewFactory.newNode(payloadFunction.apply(node)))
                    .collect(Collectors.toList());
            return newIndependentComponents(nodeViews);
        }

        var decompose = new DAGraphDecomposer<>(graph).decompose();
        var components = decompose.entrySet()
                .stream()
                .map(this::renderIndependentComponent)
                .collect(Collectors.toList());

        return newIndependentComponents(components);
    }


    COMP renderIndependentComponent(Map.Entry<Set<N>, DAGraph<N>> rootsWithSubgraph) {
        var roots = rootsWithSubgraph.getKey();
        var subGraph = rootsWithSubgraph.getValue();
        var subGraphView = doRender(subGraph);

        var rootsViews = roots.stream()
                .map(r -> viewFactory.newNode(payloadFunction.apply(r)))
                .collect(Collectors.toList());
        if (subGraphView == null) return newIndependentComponents(rootsViews);

        if (rootsViews.size() == 1) {
            return viewFactory.newFanout(rootsViews.get(0), subGraphView);
        } else {
            return viewFactory.newDependentComponents(rootsViews, subGraphView);
        }
    }

    private COMP newIndependentComponents(List<? extends COMP> components) {
        if (components.size() == 1) {
            return components.get(0);
        } else {
            return viewFactory.newIndependentComponents(components);
        }
    }
}
