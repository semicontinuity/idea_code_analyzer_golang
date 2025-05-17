package semicontinuity.idea.code.analyzer.graph;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Direct acyclic graph abstraction.
 * @param <V> the type of the vertex in the graph, must be hashable
 *            it can be just the ID of the vertex, with the node payload kept separately.
 */
public interface DAGraph<V> {

    void addVertex(V src);

    boolean hasVertices();

    Set<V> vertices();

    boolean containsVertex(V vertex);

    void forEachVertex(Consumer<V> consumer);


    void addEdge(V src, V dst);

    boolean hasEdges();

    int incomingEdgeCount(V vertex);

    void forEachEdge(BiConsumer<V, V> consumer);


    void forEachUpstreamVertex(V vertex, Consumer<V> consumer);
    void forEachDownstreamVertex(V vertex, Consumer<V> consumer);

    List<V> findRoots();

    Set<V> followers(V vertex);
}
