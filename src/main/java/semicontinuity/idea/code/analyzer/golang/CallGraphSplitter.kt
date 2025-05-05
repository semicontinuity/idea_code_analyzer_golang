package semicontinuity.idea.code.analyzer.golang;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import semicontinuity.idea.code.analyzer.graph.DAGraph;

/**
 * Splits call graph into several sub-graphs, one per struct.
 */
public class CallGraphSplitter {

    public static Map<String, DAGraph<Node>> split(DAGraph<Node> graph, Supplier<DAGraph<Node>> subGraphFactory) {
        var result = new HashMap<String, DAGraph<Node>>();

        graph.forEachNode(n -> result.computeIfAbsent(n.getQualifier(), k -> subGraphFactory.get()).addNode(n));

        graph.forEachEdge((Node n1, Node n2) -> {
            if (Objects.equals(n1.getQualifier(), n2.getQualifier())) {
                result.computeIfAbsent(n1.getQualifier(), k -> subGraphFactory.get()).addEdge(n1, n2);
            }
        });

        return result;
    }
}
