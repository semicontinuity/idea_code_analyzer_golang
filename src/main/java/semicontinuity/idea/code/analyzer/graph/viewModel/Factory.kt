package semicontinuity.idea.code.analyzer.graph.viewModel

interface Factory<VERTEX_PAYLOAD, COMP, IND_COMPS : COMP, VERTEX : COMP, SPLIT : COMP, LAYER : COMP, SPLIT0 : COMP> {

    fun independentCompsIfManyOrNullIfEmpty(items: List<COMP?>): COMP? =
        if (items.isEmpty()) {
            null
        } else {
            independentCompsOrFirst(items)
        }

    fun independentCompsOrFirst(items: List<COMP?>) =
        if (items.size == 1) {
            items[0]
        } else {
            newIndependentComponents(items)
        }

    fun newIndependentComponents(components: List<COMP?>): IND_COMPS

    fun newVertex(payload: VERTEX_PAYLOAD): VERTEX

    fun newSplit(items: List<VERTEX>, subLayer: COMP): SPLIT

    fun newSplit(left: COMP, right: COMP): SPLIT0

    fun newLayer(directDeps: COMP?, sharedDeps: COMP?): LAYER
}
