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
        return doRenderGraphLayers(graph)
    }
}
