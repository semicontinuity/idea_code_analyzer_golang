package semicontinuity.idea.code.analyzer.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphImpl<N> implements Graph<N> {

    private final HashMap<N, Set<N>> fwdEdges = new HashMap<>();
    private final HashMap<N, Set<N>> revEdges = new HashMap<>();


    @Override
    public void addEdge(N src, N dst) {
        fwdEdges.computeIfAbsent(src, (k) -> new HashSet<>()).add(dst);
        revEdges.computeIfAbsent(dst, (k) -> new HashSet<>()).add(src);

        fwdEdges.computeIfAbsent(dst, (k) -> new HashSet<>());
        revEdges.computeIfAbsent(src, (k) -> new HashSet<>());
    }

    @Override
    public List<N> findRoots() {
        return revEdges.entrySet().stream()
                .filter(entry -> entry.getValue().size() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
