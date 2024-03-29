package semicontinuity.idea.code.analyzer.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DAGraphImpl<N> implements DAGraph<N> {

    boolean hasEdges;

    private final HashSet<N> nodes = new HashSet<>();

    private final HashMap<N, Set<N>> fwdEdges = new HashMap<>();
    private final HashMap<N, Set<N>> revEdges = new HashMap<>();


    @Override
    public void addNode(N n) {
        fwdEdges.computeIfAbsent(n, (k) -> new HashSet<>());
        revEdges.computeIfAbsent(n, (k) -> new HashSet<>());
        nodes.add(n);
    }

    @Override
    public boolean hasNodes() {
        return !nodes.isEmpty();
    }

    @Override
    public Set<N> nodes() {
        return nodes;
    }

    @Override
    public boolean containsNode(N node) {
        return nodes.contains(node);
    }

    @Override
    public void forEachNode(Consumer<N> consumer) {
        nodes.forEach(consumer);
    }

    @Override
    public void addEdge(N src, N dst) {
        addNode(src);
        addNode(dst);
        fwdEdges.computeIfAbsent(src, (k) -> new HashSet<>()).add(dst);
        revEdges.computeIfAbsent(dst, (k) -> new HashSet<>()).add(src);
        hasEdges = true;
    }

    @Override
    public boolean hasEdges() {
        return hasEdges;
    }

    @Override
    public void forEachEdge(BiConsumer<N, N> consumer) {
        for (Map.Entry<N, Set<N>> e : fwdEdges.entrySet()) {
            for (N n : e.getValue()) {
                consumer.accept(e.getKey(), n);
            }
        }
    }


    @Override
    public int incomingEdgeCount(N node) {
        var edges = revEdges.get(node);
        return edges == null ? 0 : edges.size();
    }

    @Override
    public void forEachUpstreamNode(N node, Consumer<N> consumer) {
        revEdges.get(node).forEach(consumer);
    }

    @Override
    public void forEachDownstreamNode(N node, Consumer<N> consumer) {
        fwdEdges.get(node).forEach(consumer);
    }


    @Override
    public List<N> findRoots() {
        return revEdges.entrySet().stream()
                .filter(entry -> entry.getValue().size() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public Set<N> followers(N node) {
        return fwdEdges.get(node);
    }


    @Override
    public String toString() {
        return fwdEdges.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DAGraphImpl<?> graph = (DAGraphImpl<?>) o;

        if (!fwdEdges.equals(graph.fwdEdges)) {
            return false;
        }
        return revEdges.equals(graph.revEdges);
    }

    @Override
    public int hashCode() {
        int result = fwdEdges.hashCode();
        result = 31 * result + revEdges.hashCode();
        return result;
    }
}
