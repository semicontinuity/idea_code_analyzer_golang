package semicontinuity.idea.code.analyzer.golang;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import semicontinuity.idea.code.analyzer.graph.DAGraph;

/**
 * Splits Structure into several sub-graphs, one per struct.
 */
public class StructureSplitter {

    public static Map<String, DAGraph<String>> split(Structure graph, Supplier<DAGraph<String>> subGraphFactory) {
        var result = new HashMap<String, DAGraph<String>>();

        graph.forEachNode(n -> result.computeIfAbsent(n.getQualifier(), k -> subGraphFactory.get()).addNode(n.getName()));

        graph.forEachCall((QualifiedName n1, QualifiedName n2) -> {
            if (Objects.equals(n1.getQualifier(), n2.getQualifier())) {
                result.computeIfAbsent(n1.getQualifier(), k -> subGraphFactory.get()).addEdge(n1.getName(), n2.getName());
            }
        });

        return result;
    }
}
