package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory

class VFactory<PAYLOAD> :
    Factory<PAYLOAD, VComponent?, VIndependentComponents?, VMember<PAYLOAD>?, VSplit<PAYLOAD>?, VLayer?, VSplit0> {

    override fun newIndependentComponents(components: List<VComponent?>): VIndependentComponents {
        return VIndependentComponents(components)
    }

    override fun newVertex(payload: PAYLOAD): VMember<PAYLOAD> {
        return VMember(payload)
    }

    override fun newSplit(left: VComponent?, right: VComponent?): VSplit0 {
        return VSplit0(left, right)
    }

    override fun newSplit(items: List<VMember<PAYLOAD>?>, subLayer: VComponent?): VSplit<PAYLOAD> {
        return VSplit(items, subLayer)
    }

    override fun newLayer(directDeps: VComponent?, sharedDeps: VComponent?): VLayer {
        return VLayer(directDeps, sharedDeps)
    }
}
