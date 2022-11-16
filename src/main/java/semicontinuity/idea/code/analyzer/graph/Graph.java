package semicontinuity.idea.code.analyzer.graph;

import java.util.List;

public interface Graph<N> {

    void addEdge(N src, N dst);

    List<N> findRoots();
}
