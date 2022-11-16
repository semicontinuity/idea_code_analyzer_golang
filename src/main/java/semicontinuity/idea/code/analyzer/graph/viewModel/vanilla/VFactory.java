package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import java.util.List;

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory;

public class VFactory<PAYLOAD> implements Factory<
        PAYLOAD, VComponent, VIndependentComponents, VFanOut<PAYLOAD>, VDependentComponents<PAYLOAD>, VNode<PAYLOAD>> {

    @Override
    public VIndependentComponents newIndependentComponents(List<VComponent> components) {
        return new VIndependentComponents(components);
    }

    @Override
    public VFanOut<PAYLOAD> newFanout(VNode<PAYLOAD> head, List<VComponent> followers) {
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
}
