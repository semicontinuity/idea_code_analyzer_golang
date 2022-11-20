package semicontinuity.idea.code.analyzer.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Splits DAGraph into several sub-graphs, according to a provided classifier.
 * Edge of the source graph is transferred to a su-graph, if both of its nodes pertain to the same sub-graph.
 */
public class DAGraphSplitter<N, K> {

    private final DAGraph<N> graph;
    private final Function<N, K> classifier;
    private final Supplier<DAGraph<N>> subGraphFactory = DAGraphImpl::new;

    public DAGraphSplitter(DAGraph<N> graph, Function<N, K> classifier) {
        this.graph = graph;
        this.classifier = classifier;
    }

    public Map<K, DAGraph<N>> run() {
        var result = new HashMap<K, DAGraph<N>>();

        graph.forEachNode(n -> result.computeIfAbsent(classifier.apply(n), k -> subGraphFactory.get()).addNode(n));

        graph.forEachEdge((n1, n2) -> {
            var k1 = classifier.apply(n1);
            var k2 = classifier.apply(n2);
            if (k1 == k2) {
                result.computeIfAbsent(k1, k -> subGraphFactory.get()).addEdge(n1, n2);
            }
        });

        return result;
    }
}
