package semicontinuity.idea.code.analyzer.graph;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Direct acyclic graph abstraction.
 * @param <N> the type of the node in the graph, must be hashable
 *           it can be just the ID of the node, with the node payload kept separately.
 */
public interface DAGraph<N> {

    void addNode(N src);

    boolean hasNodes();

    Set<N> nodes();

    boolean containsNode(N node);

    void forEachNode(Consumer<N> consumer);


    void addEdge(N src, N dst);

    boolean hasEdges();

    int incomingEdgeCount(N node);

    void forEachEdge(BiConsumer<N, N> consumer);


    void forEachUpstreamNode(N node, Consumer<N> consumer);

    List<N> findRoots();

    Set<N> followers(N node);
}
