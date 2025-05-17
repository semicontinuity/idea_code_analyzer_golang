package semicontinuity.idea.code.analyzer.graph;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory;

public class DAGraphViewRenderer<
        V,
        VERTEX_PAYLOAD,
        COMP,
        IND_COMPS extends COMP,
        VERTEX extends COMP,
        SPLIT extends COMP,
        LAYER extends COMP,
        SORT_KEY extends Comparable<SORT_KEY>> {

    private final DAGraph<V> graph;
    private final Factory<VERTEX_PAYLOAD, COMP, IND_COMPS, VERTEX, SPLIT, LAYER> viewFactory;
    private final Function<V, VERTEX_PAYLOAD> payloadFunction;
    private final Function<V, SORT_KEY> sortKeyFunction;
    private final Set<V> multiPredecessorVertices;

    public DAGraphViewRenderer(
            DAGraph<V> graph,
            Factory<VERTEX_PAYLOAD, COMP, IND_COMPS, VERTEX, SPLIT, LAYER> viewFactory,
            Function<V, VERTEX_PAYLOAD> payloadFunction,
            Function<V, SORT_KEY> sortKeyFunction) {
        this.graph = graph;
        this.viewFactory = viewFactory;
        this.payloadFunction = payloadFunction;
        this.multiPredecessorVertices = graph.vertices().stream().filter(v -> graph.incomingEdgeCount(v) > 1).collect(Collectors.toSet());
        this.sortKeyFunction = sortKeyFunction;
    }


    public COMP render() {
        var view = doRender(graph);
        if (view == null) return viewFactory.newIndependentComponents(List.of());
        return view;
    }

    private COMP doRender(DAGraph<V> graph) {
        if (!graph.hasVertices()) return null;

        System.out.println("=================================================================================================");
        System.out.println("doRender graph; " + graph.vertices().size() + " vertices=" + graph.vertices());
        System.out.println("=================================================================================================");

        if (!graph.hasEdges()) {
            System.out.println("| doRender: no edges");
            var direct = vertexViews(graph, false);
            var shared = vertexViews(graph, true);
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

    COMP renderRootsWithSubgraph(Map.Entry<Set<V>, DAGraph<V>> rootsWithSubgraph) {
        var roots = rootsWithSubgraph.getKey();
        var subGraph = rootsWithSubgraph.getValue();

        var rootsViews = roots.stream()
                .sorted(Comparator.comparing(sortKeyFunction))
                .map((V r) -> {
                    var payload = payloadFunction.apply(r);
                    System.out.println(" renderRootsWithSubgraph: root=" + payload);
                    return viewFactory.newVertex(payload);
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


    private List<VERTEX> vertexViews(DAGraph<V> graph, boolean isMultiVertex) {
        return graph.vertices()
                .stream()
                .filter(v -> multiPredecessorVertices.contains(v) == isMultiVertex)
                .sorted(Comparator.comparing(sortKeyFunction))
                .map(vertex -> {
                    var payload = payloadFunction.apply(vertex);
                    return viewFactory.newVertex(payload);
                })
                .collect(Collectors.toList());
    }
}
