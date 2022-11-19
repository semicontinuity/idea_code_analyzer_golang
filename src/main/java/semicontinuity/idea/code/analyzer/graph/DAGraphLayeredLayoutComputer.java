package semicontinuity.idea.code.analyzer.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAGraphLayeredLayoutComputer<N> {

    private final DAGraph<N> graph;

    public DAGraphLayeredLayoutComputer(DAGraph<N> graph) {
        this.graph = graph;
    }

    public Map<N, Integer> layout() {
        return layoutFrom(graph.findRoots());
    }

    public Map<N, Integer> layoutFrom(List<N> roots) {
        var depths = new HashMap<N, Integer>();
        for (N root : roots) {
            setDepth(0, root, depths);
        }
        return depths;
    }

    private void setDepth(int depth, N node, Map<N, Integer> depths) {
        var currentDepth = depths.get(node);
        if (currentDepth == null || currentDepth < depth) {
            depths.put(node, depth);
            for (N n : graph.followers(node)) {
                setDepth(depth + 1, n, depths);
            }
        }
    }
}
