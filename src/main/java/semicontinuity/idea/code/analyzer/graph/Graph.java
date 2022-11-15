package semicontinuity.idea.code.analyzer.graph;

import java.util.Set;

public interface Graph<ID, N> {

    void addNode(ID id, N node);

    void addEdge(ID idFrom, ID idTo);

    Set<ID> findRootEdges();
}
