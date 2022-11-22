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
        NODE extends COMP,
        SPLIT extends COMP,
        LAYER extends COMP> {

    private final DAGraph<N> graph;
    private final Factory<NODE_PAYLOAD, COMP, IND_COMPS, FANOUT, DEP_COMPS, NODE, SPLIT, LAYER> viewFactory;
    private final Function<N, NODE_PAYLOAD> payloadFunction;
    private final Set<N> multiNodes;

    public DAGraphViewRenderer(
            DAGraph<N> graph,
            Factory<NODE_PAYLOAD, COMP, IND_COMPS, FANOUT, DEP_COMPS, NODE, SPLIT, LAYER> viewFactory,
            Function<N, NODE_PAYLOAD> payloadFunction) {
        this.graph = graph;
        this.viewFactory = viewFactory;
        this.payloadFunction = payloadFunction;
        this.multiNodes = graph.nodes().stream().filter(n -> graph.incomingEdgeCount(n) > 1).collect(Collectors.toSet());
    }


    public COMP render() {
        var view = doRender(graph);
        if (view == null) return viewFactory.newIndependentComponents(List.of());
        return view;
    }

    private COMP doRender(DAGraph<N> graph) {
        if (!graph.hasNodes()) return null;

        if (!graph.hasEdges()) {
            var direct = nodeViews(graph, false);
            var shared = nodeViews(graph, true);
            return viewFactory.newLayer(independentCompsIfManyOrNullIfZero(direct), independentCompsIfManyOrNullIfZero(shared));
        }

        var decompose = new DAGraphDecomposer<>(graph).decompose();
        var components = decompose.entrySet()
                .stream()
                .map(this::renderRootsWithSubgraph)
                .collect(Collectors.toList());

        return independentCompsIfManyOrNullIfZero(components);
    }

    COMP renderRootsWithSubgraph(Map.Entry<Set<N>, DAGraph<N>> rootsWithSubgraph) {
        var roots = rootsWithSubgraph.getKey();
        var subGraph = rootsWithSubgraph.getValue();

        var rootsViews = roots.stream()
                .map(r -> viewFactory.newNode(payloadFunction.apply(r)))
                .collect(Collectors.toList());

        var subGraphView = doRender(subGraph);
        if (subGraphView == null) return independentCompsIfManyOrNullIfZero(rootsViews);

        return viewFactory.newSplit(rootsViews, subGraphView);
    }

    private COMP independentCompsIfManyOrNullIfZero(List<? extends COMP> items) {
        if (items.size() == 0) {
            return null;
        } else if (items.size() == 1) {
            return items.get(0);
        } else {
            return viewFactory.newIndependentComponents(items);
        }
    }


    private List<NODE> nodeViews(DAGraph<N> graph, boolean isMultiNode) {
        return graph.nodes()
                .stream()
                .filter(n -> multiNodes.contains(n) == isMultiNode)
                .map(node -> viewFactory.newNode(payloadFunction.apply(node)))
                .collect(Collectors.toList());
    }
}
