package semicontinuity.idea.code.analyzer.graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Direct acyclic graph abstraction.
 * @param <N> the type of the node in the graph, must be hashable
 *           it can be just the ID of the node, with the node payload kept separately.
 */
public interface DAGraph<N> {

    Set<N> nodes();

    boolean hasEdges();

    boolean hasNodes();

    void addNode(N src);

    void addEdge(N src, N dst);

    List<N> findRoots();

    Map<Set<N>, DAGraph<N>> decompose();

    Map<Set<N>, DAGraph<N>> decomposeFrom(List<N> roots);
}
