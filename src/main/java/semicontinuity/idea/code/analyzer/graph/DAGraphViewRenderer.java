package semicontinuity.idea.code.analyzer.graph;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory;

public class DAGraphViewRenderer<
        N,
        NODE_PAYLOAD,
        COMP,
        IND_COMPS extends COMP,
        NODE extends COMP,
        SPLIT extends COMP,
        LAYER extends COMP,
        SORT_KEY extends Comparable<SORT_KEY>> {

    private final DAGraph<N> graph;
    private final Factory<NODE_PAYLOAD, COMP, IND_COMPS, NODE, SPLIT, LAYER> viewFactory;
    private final Function<N, NODE_PAYLOAD> payloadFunction;
    private final Function<N, SORT_KEY> sortKeyFunction;
    private final Set<N> multiNodes;

    public DAGraphViewRenderer(
            DAGraph<N> graph,
            Factory<NODE_PAYLOAD, COMP, IND_COMPS, NODE, SPLIT, LAYER> viewFactory,
            Function<N, NODE_PAYLOAD> payloadFunction,
            Function<N, SORT_KEY> sortKeyFunction) {
        this.graph = graph;
        this.viewFactory = viewFactory;
        this.payloadFunction = payloadFunction;
        this.multiNodes = graph.nodes().stream().filter(n -> graph.incomingEdgeCount(n) > 1).collect(Collectors.toSet());
        this.sortKeyFunction = sortKeyFunction;
    }


    public COMP render() {
        var view = doRender(graph);
        if (view == null) return viewFactory.newIndependentComponents(List.of());
        return view;
    }

    private COMP doRender(DAGraph<N> graph) {
        if (!graph.hasNodes()) return null;

        System.out.println("=================================================================================================");
        System.out.println("doRender graph; " + graph.nodes().size() + " nodes=" + graph.nodes());
        System.out.println("=================================================================================================");

        if (!graph.hasEdges()) {
            System.out.println("| doRender: no edges");
            var direct = nodeViews(graph, false);
            var shared = nodeViews(graph, true);
            return viewFactory.newLayer(independentCompsIfManyOrNullIfZero(direct), independentCompsIfManyOrNullIfZero(shared));
        } else {
            var decomposed = new DAGraphDecomposer<>(graph).decompose();
            System.out.println("| doRender: graph decomposed into " + decomposed.size());
            var components = decomposed.entrySet()
                    .stream()
                    .map(this::renderRootsWithSubgraph)
                    .collect(Collectors.toList());

            return independentCompsIfManyOrNullIfZero(components);
        }
    }

    COMP renderRootsWithSubgraph(Map.Entry<Set<N>, DAGraph<N>> rootsWithSubgraph) {
        var roots = rootsWithSubgraph.getKey();
        var subGraph = rootsWithSubgraph.getValue();

        var rootsViews = roots.stream()
                .sorted(Comparator.comparing(sortKeyFunction))
                .map((N r) -> {
                    var payload = payloadFunction.apply(r);
                    System.out.println(" renderRootsWithSubgraph: root=" + payload);
                    return viewFactory.newNode(payload);
                })
                .collect(Collectors.toList());

        var subGraphView = doRender(subGraph);
        if (subGraphView == null) return independentCompsIfManyOrNullIfZero(rootsViews);

        return viewFactory.newSplit(rootsViews, subGraphView);
    }

    private COMP independentCompsIfManyOrNullIfZero(List<? extends COMP> items) {
        if (items.isEmpty()) {
            return null;
        } else if (items.size() == 1) {
            return items.get(0);
        } else {
            return viewFactory.newIndependentComponents(items);
        }
    }


    private List<NODE> nodeViews(DAGraph<N> graph, boolean isMultiNode) {
        System.out.println("  === nodeViews isMultiNode=" + isMultiNode);
        return graph.nodes()
                .stream()
                .filter(n -> multiNodes.contains(n) == isMultiNode)
                .sorted(Comparator.comparing(sortKeyFunction))
                .map(node -> {
                    var payload = payloadFunction.apply(node);
                    System.out.println("  ===   nodeViews " + payload);
                    return viewFactory.newNode(payload);
                })
                .collect(Collectors.toList());
    }
}
