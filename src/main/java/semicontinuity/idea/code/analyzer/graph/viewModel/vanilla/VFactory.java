package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import java.util.List;

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory;

public class VFactory<PAYLOAD> extends VComponent implements Factory<
        PAYLOAD,
        VComponent,
        VIndependentComponents,
        VFanOut<PAYLOAD>,
        VDependentComponents<PAYLOAD>,
        VNode<PAYLOAD>,
        VSplit<PAYLOAD>,
        VLayer
        > {

    @Override
    public VIndependentComponents newIndependentComponents(List<? extends VComponent> components) {
        return new VIndependentComponents(components);
    }

    @Override
    public VFanOut<PAYLOAD> newFanout(VNode<PAYLOAD> head, VComponent followers) {
        return new VFanOut<>(head, followers);
    }

    @Override
    public VDependentComponents<PAYLOAD> newDependentComponents(List<VNode<PAYLOAD>> heads, VComponent deeperLayers) {
        return new VDependentComponents<>(heads, deeperLayers);
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
