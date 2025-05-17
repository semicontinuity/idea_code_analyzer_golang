package semicontinuity.idea.code.analyzer.graph

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory
import java.util.function.Function
import java.util.stream.Collectors

class DAGraphViewRenderer<V, VERTEX_PAYLOAD, COMP, IND_COMPS : COMP, VERTEX : COMP, SPLIT : COMP, LAYER : COMP, SORT_KEY : Comparable<SORT_KEY>?>(
    private val graph: DAGraph<V>,
    viewFactory: Factory<VERTEX_PAYLOAD, COMP, IND_COMPS, VERTEX, SPLIT, LAYER>,
    payloadFunction: Function<V, VERTEX_PAYLOAD>,
    sortKeyFunction: Function<V, SORT_KEY>
) {
    private val viewFactory: Factory<VERTEX_PAYLOAD, COMP, IND_COMPS, VERTEX, SPLIT, LAYER>
    private val payloadFunction: Function<V, VERTEX_PAYLOAD>
    private val sortKeyFunction: Function<V, SORT_KEY>
    private val multiPredecessorVertices: Set<V>

    init {
        this.viewFactory = viewFactory
        this.payloadFunction = payloadFunction
        this.multiPredecessorVertices =
            graph.vertices().stream().filter { v: V -> graph.incomingEdgeCount(v) > 1 }.collect(
                Collectors.toSet()
            )
        this.sortKeyFunction = sortKeyFunction
    }

    fun render(): COMP {
        val view = doRender(graph) ?: return viewFactory.newIndependentComponents(listOf<COMP>())
        return view
    }

    private fun doRender(graph: DAGraph<V>): COMP? {
        if (!graph.hasVertices()) return null

        println("=================================================================================================")
        println("doRender graph; " + graph.size() + " vertices=" + graph.vertices())
        println("=================================================================================================")

        if (!graph.hasEdges()) {
            println("| doRender: no edges")
            val direct = vertexViews(graph, false)
            val shared = vertexViews(graph, true)
            return viewFactory.newLayer(
                independentCompsIfManyOrNullIfZero(direct),
                independentCompsIfManyOrNullIfZero(shared)
            )
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
                .map { this.renderRootsWithSubgraph(it) }

            return independentCompsIfManyOrNullIfZero(components)
        }
    }

    private fun renderRootsWithSubgraph(rootsWithSubgraph: Map.Entry<Set<V>, DAGraph<V>>): COMP? {
        val roots = rootsWithSubgraph.key
        val subGraph = rootsWithSubgraph.value

        val rootsViews = roots.stream()
            .sorted(Comparator.comparing<V, SORT_KEY>(sortKeyFunction))
            .map { r: V ->
                val payload = payloadFunction.apply(r)
                println(" renderRootsWithSubgraph: root=$payload")
                viewFactory.newVertex(payload)
            }
            .collect(Collectors.toList())

        return when (val subGraphView = doRender(subGraph)) {
            null -> independentCompsIfManyOrNullIfEmpty(rootsViews)
            else -> viewFactory.newSplit(rootsViews, subGraphView)
        }
    }
    private fun independentCompsIfManyOrNullIfEmpty(items: List<COMP?>): COMP? =
        if (items.isEmpty()) {
            null
        } else {
            independentCompsOrFirst(items)
        }

    private fun independentCompsOrFirst(items: List<COMP?>) =
        if (items.size == 1) {
            items[0]
        } else {
            viewFactory.newIndependentComponents(items)
        }

    private fun independentCompsIfManyOrNullIfZero(items: List<COMP?>): COMP? {
        return if (items.isEmpty()) {
            null
        } else if (items.size == 1) {
            items[0]
        } else {
            viewFactory.newIndependentComponents(items)
        }
    }

    private fun vertexViews(graph: DAGraph<V>, isMultiVertex: Boolean): List<VERTEX> {
        return graph.vertices()
            .stream()
            .filter { v: V -> multiPredecessorVertices.contains(v) == isMultiVertex }
            .sorted(Comparator.comparing(sortKeyFunction))
            .map { vertex: V ->
                val payload = payloadFunction.apply(vertex)
                viewFactory.newVertex(payload)
            }
            .collect(Collectors.toList())
    }
}
