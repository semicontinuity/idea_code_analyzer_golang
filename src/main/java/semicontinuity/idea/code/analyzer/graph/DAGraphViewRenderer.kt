package semicontinuity.idea.code.analyzer.graph

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory
import java.util.function.Function
import java.util.stream.Collectors

abstract class DAGraphViewRenderer<V, VERTEX_PAYLOAD, COMP, IND_COMPS : COMP, VERTEX : COMP, SPLIT : COMP, LAYER : COMP, SORT_KEY : Comparable<SORT_KEY>?>(
    protected val viewFactory: Factory<VERTEX_PAYLOAD, COMP, IND_COMPS, VERTEX, SPLIT, LAYER>,
    protected val payloadFunction: Function<V, VERTEX_PAYLOAD>,
    protected val sortKeyFunction: Function<V, SORT_KEY>,
) {
    fun render(daGraph: DAGraph<V>): COMP =
        doRender(daGraph) ?: viewFactory.newIndependentComponents(listOf<COMP>())

    private fun doRender(graph: DAGraph<V>): COMP? {
        if (!graph.hasVertices()) return null

        println("=================================================================================================")
        println("doRender graph; " + graph.vertexCount() + " vertices=" + graph.vertices())
        println("=================================================================================================")

        return if (!graph.hasEdges()) {
            println("| doRender: no edges")
            val multiPredecessorVertices: Set<V> =
                graph.vertices().stream()
                    .filter { v: V -> graph.predecessorCount(v) > 1 }
                    .collect(Collectors.toSet())

            val direct = viewFactory.independentCompsIfManyOrNullIfEmpty(vertexViews(
                graph,
                false,
                multiPredecessorVertices
            ))
            val shared = viewFactory.independentCompsIfManyOrNullIfEmpty(vertexViews(
                graph,
                true,
                multiPredecessorVertices
            ))

            when {
                direct == null -> return shared
                shared == null -> return direct
                else -> return viewFactory.newLayer(
                    direct,
                    shared
                )
            }
        } else {
            doRenderGraphWithEdges(graph)
        }
    }

    abstract fun doRenderGraphWithEdges(graph: DAGraph<V>): COMP?

    private fun vertexViews(graph: DAGraph<V>, isMultiVertex: Boolean, multiVertices: Set<V>): List<VERTEX> =
        graph.vertices()
            .stream()
            .filter { v: V -> multiVertices.contains(v) == isMultiVertex }
            .sorted(Comparator.comparing(sortKeyFunction))
            .map { vertex: V ->
                viewFactory.newVertex(payloadFunction.apply(vertex))
            }
            .collect(Collectors.toList())
}
