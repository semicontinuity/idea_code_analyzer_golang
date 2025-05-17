package semicontinuity.idea.code.analyzer.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAGraphLayeredLayoutComputer<V> {

    private final DAGraph<V> graph;

    public DAGraphLayeredLayoutComputer(DAGraph<V> graph) {
        this.graph = graph;
    }

    public Map<V, Integer> layout() {
        return layoutFrom(graph.rootList());
    }

    public Map<V, Integer> layoutFrom(List<V> roots) {
        var depths = new HashMap<V, Integer>();
        for (V root : roots) {
            setDepth(0, root, depths);
        }
        return depths;
    }

    private void setDepth(int depth, V vertex, Map<V, Integer> depths) {
        var currentDepth = depths.get(vertex);
        if (currentDepth == null || currentDepth < depth) {
            depths.put(vertex, depth);
            for (V v : graph.followers(vertex)) {
                setDepth(depth + 1, v, depths);
            }
        }
    }
}
