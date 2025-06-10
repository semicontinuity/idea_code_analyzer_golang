package semicontinuity.idea.code.analyzer.graph

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory
import java.util.function.Function
import java.util.stream.Collectors

class DAGraphViewRenderer1<V, VERTEX_PAYLOAD, COMP, IND_COMPS : COMP, VERTEX : COMP, SPLIT : COMP, LAYER : COMP, SORT_KEY : Comparable<SORT_KEY>?>(
    viewFactory: Factory<VERTEX_PAYLOAD, COMP, IND_COMPS, VERTEX, SPLIT, LAYER>,
    payloadFunction: Function<V, VERTEX_PAYLOAD>,
    sortKeyFunction: Function<V, SORT_KEY>,
) : DAGraphViewRenderer<V, VERTEX_PAYLOAD, COMP, IND_COMPS, VERTEX, SPLIT, LAYER, SORT_KEY>(
    viewFactory,
    payloadFunction,
    sortKeyFunction
) {
    override fun doRenderGraphWithEdges(graph: DAGraph<V>): COMP? {
        val decomposed = DAGraphDecomposer(graph).decompose()
        println("| doRender: graph decomposed into " + decomposed.size)
        val components = decomposed.entries
            .map { (roots, subGraph) ->
                this.renderRootsWithSubgraph(roots, subGraph)
            }

        return viewFactory.independentCompsIfManyOrNullIfEmpty(components)
    }

    private fun renderRootsWithSubgraph(roots: Set<V>, subGraph: DAGraph<V>): COMP? {
        val rootsViews = roots.stream()
            .sorted(Comparator.comparing<V, SORT_KEY>(sortKeyFunction))
            .map { r: V ->
                val payload = payloadFunction.apply(r)
                println(" renderRootsWithSubgraph: root=$payload")
                viewFactory.newVertex(payload)
            }
            .collect(Collectors.toList())

        return when (val subGraphView = doRenderGraphWithEdges(subGraph)) {
            null -> viewFactory.independentCompsIfManyOrNullIfEmpty(rootsViews)
            else -> viewFactory.newSplit(rootsViews, subGraphView)
        }
    }
}
