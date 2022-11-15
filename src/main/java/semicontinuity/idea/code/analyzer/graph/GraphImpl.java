package semicontinuity.idea.code.analyzer.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphImpl<ID, N> implements Graph<ID, N> {

    private final HashMap<ID, N> nodes = new HashMap<>();
    private final HashMap<ID, Set<ID>> fwdEdges = new HashMap<>();
    private final HashMap<ID, Set<ID>> revEdges = new HashMap<>();

    @Override
    public void addNode(ID id, N node) {
        nodes.put(id, node);
        fwdEdges.computeIfAbsent(id, (k) -> new HashSet<>());
        revEdges.computeIfAbsent(id, (k) -> new HashSet<>());
    }

    @Override
    public void addEdge(ID idFrom, ID idTo) {
        fwdEdges.get(idFrom).add(idTo);
        revEdges.get(idTo).add(idFrom);
    }

    @Override
    public Set<ID> findRootEdges() {
        return revEdges.entrySet().stream()
                .filter(entry -> entry.getValue().size() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
