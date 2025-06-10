package semicontinuity.idea.code.analyzer.graph

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory
import java.util.function.Function
import java.util.stream.Collectors

class DAGraphViewRenderer1<V, VERTEX_PAYLOAD, COMP, IND_COMPS : COMP, VERTEX : COMP, SPLIT : COMP, LAYER : COMP, SORT_KEY : Comparable<SORT_KEY>?, SPLIT0 : COMP>(
    viewFactory: Factory<VERTEX_PAYLOAD, COMP, IND_COMPS, VERTEX, SPLIT, LAYER, SPLIT0>,
    payloadFunction: Function<V, VERTEX_PAYLOAD>,
    sortKeyFunction: Function<V, SORT_KEY>,
) : DAGraphViewRenderer<V, VERTEX_PAYLOAD, COMP, IND_COMPS, VERTEX, SPLIT, LAYER, SORT_KEY, SPLIT0>(
    viewFactory,
    payloadFunction,
    sortKeyFunction
) {
    override fun doRenderGraphWithEdges(graph: DAGraph<V>): COMP {
        val decomposed: Map<Set<V>, DAGraph<V>> = DAGraphDecomposer(graph).decompose()

        val views = decomposed.entries
            .map { (roots, subGraph) ->
                this.renderRootsWithSubgraph(roots, subGraph)
            }

        return viewFactory.independentCompsOrFirst(views)
    }

    private fun renderRootsWithSubgraph(roots: Set<V>, subGraph: DAGraph<V>): COMP =
        when (subGraph.hasVertices()) {
            true -> viewFactory.newSplit(sortedVerticesViews(roots), doRenderNonEmptyGraph(subGraph))
            false -> viewFactory.independentCompsOrFirst(sortedVerticesViews(roots))
        }

    private fun sortedVerticesViews(roots: Set<V>): List<VERTEX> =
        roots.stream()
            .sorted(Comparator.comparing<V, SORT_KEY>(sortKeyFunction))
            .map { r: V ->
                val payload = payloadFunction.apply(r)
                println(" renderRootsWithSubgraph: root=$payload")
                viewFactory.newVertex(payload)
            }
            .collect(Collectors.toList())
}
