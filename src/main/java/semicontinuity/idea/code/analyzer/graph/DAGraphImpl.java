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

    private final HashSet<N> vertices = new HashSet<>();

    private final HashMap<N, Set<N>> fwdEdges = new HashMap<>();
    private final HashMap<N, Set<N>> revEdges = new HashMap<>();


    @Override
    public void addVertex(N n) {
        fwdEdges.computeIfAbsent(n, (k) -> new HashSet<>());
        revEdges.computeIfAbsent(n, (k) -> new HashSet<>());
        vertices.add(n);
    }

    @Override
    public boolean hasVertices() {
        return !vertices.isEmpty();
    }

    @Override
    public Set<N> vertices() {
        return vertices;
    }

    @Override
    public boolean containsVertex(N vertex) {
        return vertices.contains(vertex);
    }

    @Override
    public void forEachVertex(Consumer<N> consumer) {
        vertices.forEach(consumer);
    }

    @Override
    public void addEdge(N src, N dst) {
        addVertex(src);
        addVertex(dst);
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
    public int incomingEdgeCount(N vertex) {
        var edges = revEdges.get(vertex);
        return edges == null ? 0 : edges.size();
    }

    @Override
    public void forEachUpstreamVertex(N vertex, Consumer<N> consumer) {
        revEdges.get(vertex).forEach(consumer);
    }

    @Override
    public void forEachDownstreamVertex(N vertex, Consumer<N> consumer) {
        fwdEdges.get(vertex).forEach(consumer);
    }


    @Override
    public List<N> findRoots() {
        return revEdges.entrySet().stream()
                .filter(entry -> entry.getValue().size() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public Set<N> followers(N vertex) {
        return fwdEdges.get(vertex);
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
