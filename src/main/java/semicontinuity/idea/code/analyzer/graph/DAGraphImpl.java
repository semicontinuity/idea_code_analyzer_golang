package semicontinuity.idea.code.analyzer.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DAGraphImpl<N> implements DAGraph<N> {

    boolean hasEdges;

    private final HashSet<N> nodes = new HashSet<>();

    private final HashMap<N, Set<N>> fwdEdges = new HashMap<>();
    private final HashMap<N, Set<N>> revEdges = new HashMap<>();


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
    public Set<N> nodes() {
        return nodes;
    }

    @Override
    public boolean hasNodes() {
        return !nodes.isEmpty();
    }

    @Override
    public void addNode(N n) {
        fwdEdges.computeIfAbsent(n, (k) -> new HashSet<>());
        revEdges.computeIfAbsent(n, (k) -> new HashSet<>());
        nodes.add(n);
    }

    public Map<N, Integer> layout() {
        return layoutFrom(findRoots());
    }

    public Map<N, Integer> layoutFrom(List<N> roots) {
        var depths = new HashMap<N, Integer>();
        for (N root : roots) {
            setDepth(0, root, depths);
        }
        return depths;
    }

    private void setDepth(int depth, N node, Map<N, Integer> depths) {
        var currentDepth = depths.get(node);
        if (currentDepth == null || currentDepth < depth) {
            depths.put(node, depth);
            for (N n : fwdEdges.get(node)) {
                setDepth(depth + 1, n, depths);
            }
        }
    }

    @Override
    public Map<Set<N>, DAGraph<N>> decompose() {
        return decomposeFrom(findRoots());
    }

    @Override
    public Map<Set<N>, DAGraph<N>> decomposeFrom(List<N> roots) {
        // Paint every node with the 'color' of its root.
        var colors = new HashMap<N, N>();
        for (N root : roots) {
            paintFrom(root, root, colors);
        }

        // Traverse the graph, detecting mis-colorings:
        // If the color of a node differs form the color of its root, then it has been re-painted from another root.
        // It means, that sub-graphs, starting at these roots, are connected.
        // Will track this connectedness in the 'parent' hash map.
        var parents = new HashMap<N, N>();
        for (N root : roots) {
            parents.put(root, root);
        }
        for (N root : roots) {
            checkColorFrom(root, root, colors, parents);
        }

        // Invert 'parents' to produce 'rootGroups'
        var rootGroups = parents.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toSet())));

        // Create sub-graphs for each root group
        var result = new HashMap<Set<N>, DAGraph<N>>();
        for (Set<N> rootGroup : rootGroups.values()) {
            var subGraph = new DAGraphImpl<N>();
            for (N root : rootGroup) {
                for (N nextNode : fwdEdges.get(root)) {
                    fillSubGraphFrom(nextNode, subGraph);
                }
            }
            result.put(rootGroup, subGraph);
        }

        return result;
    }

    private void paintFrom(N node, N color, HashMap<N, N> colors) {
        for (N nextNode : fwdEdges.get(node)) {
            colors.put(nextNode, color);
            paintFrom(nextNode, color, colors);
        }
    }

    private void checkColorFrom(N node, N color, HashMap<N, N> colors, HashMap<N, N> parents) {
        for (N nextNode : fwdEdges.get(node)) {
            var aColor = colors.get(nextNode);
            if (!color.equals(aColor)) {
                parents.put(aColor, color);
            }
            checkColorFrom(nextNode, color, colors, parents);
        }
    }

    void fillSubGraphFrom(N node, DAGraphImpl<N> sink) {
        sink.addNode(node);
        for (N nextNode : fwdEdges.get(node)) {
            sink.addEdge(node, nextNode);
            fillSubGraphFrom(nextNode, sink);
        }
    }

    @Override
    public List<N> findRoots() {
        return revEdges.entrySet().stream()
                .filter(entry -> entry.getValue().size() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
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
