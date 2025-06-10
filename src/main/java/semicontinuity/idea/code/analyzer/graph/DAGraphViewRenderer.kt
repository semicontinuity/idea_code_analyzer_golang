package semicontinuity.idea.code.analyzer.graph

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory
import java.util.function.Function
import java.util.stream.Collectors

abstract class DAGraphViewRenderer<V, VERTEX_PAYLOAD, COMP, IND_COMPS : COMP, VERTEX : COMP, SPLIT : COMP, LAYER : COMP, SORT_KEY : Comparable<SORT_KEY>?, SPLIT0 : COMP>(
    protected val viewFactory: Factory<VERTEX_PAYLOAD, COMP, IND_COMPS, VERTEX, SPLIT, LAYER, SPLIT0>,
    protected val payloadFunction: Function<V, VERTEX_PAYLOAD>,
    protected val sortKeyFunction: Function<V, SORT_KEY>,
) {
    fun render(daGraph: DAGraph<V>): COMP =
        doRender(daGraph) ?: viewFactory.empty()

    protected fun doRender(graph: DAGraph<V>): COMP? =
        if (graph.hasVertices()) doRenderNonEmptyGraph(graph)
        else null

    protected fun doRenderNonEmptyGraph(graph: DAGraph<V>): COMP {
        println("=================================================================================================")
        println("doRender graph; " + graph.vertexCount() + " vertices=" + graph.vertices())
        println("=================================================================================================")

        return if (graph.hasEdges()) {
            doRenderGraphWithEdges(graph)
        } else {
            doRenderGraphWithoutEdges(graph)
        }
    }

    /**
     * @param graph non-empty graph
     */
    abstract fun doRenderGraphWithEdges(graph: DAGraph<V>): COMP

    /**
     * @param graph non-empty graph
     */
    private fun doRenderGraphWithoutEdges(graph: DAGraph<V>): COMP {
        println("| doRender: no edges")
        val multiPredecessorVertices: Set<V> =
            graph.vertices().stream()
                .filter { v: V -> graph.predecessorCount(v) > 1 }
                .collect(Collectors.toSet())

        val direct = viewFactory.independentCompsIfManyOrFirstIfSingleOrNullIfEmpty(
            vertexViews(
                graph,
                false,
                multiPredecessorVertices
            )
        )
        val shared = viewFactory.independentCompsIfManyOrFirstIfSingleOrNullIfEmpty(
            vertexViews(
                graph,
                true,
                multiPredecessorVertices
            )
        )

        return when {
            direct == null -> shared!!
            shared == null -> direct
            else -> viewFactory.newLayer(
                direct,
                shared
            )
        }
    }

    private fun vertexViews(graph: DAGraph<V>, isMultiVertex: Boolean, multiVertices: Set<V>): List<VERTEX> =
        graph.vertices()
            .stream()
            .filter { v: V -> multiVertices.contains(v) == isMultiVertex }
            .sorted(Comparator.comparing(sortKeyFunction))
            .map { vertex: V ->
                viewFactory.newVertex(payloadFunction.apply(vertex))
            }
            .collect(Collectors.toList())


    protected fun doRenderGraphLayers(graph: DAGraph<V>): COMP {
        val decomposed = DAGraphDecomposer(graph).decompose()
        val views = decomposed.entries
            .map { (roots, subGraph) -> renderRootsWithSubgraph(roots, subGraph) }

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
                viewFactory.newVertex(payloadFunction.apply(r))
            }
            .collect(Collectors.toList())
}
