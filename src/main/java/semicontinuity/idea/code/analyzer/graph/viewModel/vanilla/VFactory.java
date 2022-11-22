package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import java.util.List;

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory;

public class VFactory<PAYLOAD> implements Factory<
        PAYLOAD,
        VComponent,
        VIndependentComponents,
        VNode<PAYLOAD>,
        VSplit<PAYLOAD>,
        VLayer
        > {

    @Override
    public VIndependentComponents newIndependentComponents(List<? extends VComponent> components) {
        return new VIndependentComponents(components);
    }

    @Override
    public VNode<PAYLOAD> newNode(PAYLOAD payload) {
        return new VNode<>(payload);
    }

    @Override
    public VSplit<PAYLOAD> newSplit(List<VNode<PAYLOAD>> items, VComponent subLayer) {
        return new VSplit<>(items, subLayer);
    }

    @Override
    public VLayer newLayer(VComponent directDeps, VComponent sharedDeps) {
        return new VLayer(directDeps, sharedDeps);
    }
}
