package semicontinuity.idea.code.analyzer.graph

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory
import java.util.function.Function
import java.util.stream.Collectors

class DAGraphViewRenderer<V, VERTEX_PAYLOAD, COMP, IND_COMPS : COMP, VERTEX : COMP, SPLIT : COMP, LAYER : COMP, SORT_KEY : Comparable<SORT_KEY>?>(
    private val graph: DAGraph<V>,
    private val viewFactory: Factory<VERTEX_PAYLOAD, COMP, IND_COMPS, VERTEX, SPLIT, LAYER>,
    private val payloadFunction: Function<V, VERTEX_PAYLOAD>,
    private val sortKeyFunction: Function<V, SORT_KEY>,
    private val delegate: Function<DAGraph<V>, COMP?>,
) {
    private val multiPredecessorVertices: Set<V> =
        graph.vertices().stream()
            .filter { v: V -> graph.predecessorCount(v) > 1 }
            .collect(Collectors.toSet())

    fun render(): COMP =
        doRender(graph) ?: viewFactory.newIndependentComponents(listOf<COMP>())

    private fun doRender(graph: DAGraph<V>): COMP? {
        if (!graph.hasVertices()) return null

        println("=================================================================================================")
        println("doRender graph; " + graph.vertexCount() + " vertices=" + graph.vertices())
        println("=================================================================================================")

        return if (!graph.hasEdges()) {
            println("| doRender: no edges")

            val direct = viewFactory.independentCompsIfManyOrNullIfEmpty(vertexViews(graph, false))
            val shared = viewFactory.independentCompsIfManyOrNullIfEmpty(vertexViews(graph, true))

            when {
                direct == null -> return shared
                shared == null -> return direct
                else -> return viewFactory.newLayer(
                    direct,
                    shared
                )
            }
        } else {
            val decomposed = DAGraphDecomposer(graph).decompose()
            println("| doRender: graph decomposed into " + decomposed.size)
            // val components = decomposed.entries
            //     .stream()
            //     .map { rootsWithSubgraph: Map.Entry<Set<V>, DAGraph<V>> ->
            //         this.renderRootsWithSubgraph(
            //             rootsWithSubgraph
            //         )
            //     }
            //     .collect(Collectors.toList())
            val components = decomposed.entries
                .map { (roots, subGraph) ->
                    this.renderRootsWithSubgraph(roots, subGraph)
                }

            viewFactory.independentCompsIfManyOrNullIfEmpty(components)
        }
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

        return when (val subGraphView = doRender(subGraph)) {
            null -> viewFactory.independentCompsIfManyOrNullIfEmpty(rootsViews)
            else -> viewFactory.newSplit(rootsViews, subGraphView)
        }
    }

    private fun vertexViews(graph: DAGraph<V>, isMultiVertex: Boolean): List<VERTEX> =
        graph.vertices()
            .stream()
            .filter { v: V -> multiPredecessorVertices.contains(v) == isMultiVertex }
            .sorted(Comparator.comparing(sortKeyFunction))
            .map { vertex: V ->
                viewFactory.newVertex(payloadFunction.apply(vertex))
            }
            .collect(Collectors.toList())
}
