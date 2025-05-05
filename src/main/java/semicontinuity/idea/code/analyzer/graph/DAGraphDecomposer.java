package semicontinuity.idea.code.analyzer.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DAGraphDecomposer<V> {

    private final DAGraph<V> graph;
    private final Supplier<DAGraph<V>> subGraphFactory = DAGraphImpl::new;

    public DAGraphDecomposer(DAGraph<V> graph) {
        this.graph = graph;
    }

    public Map<Set<V>, DAGraph<V>> decompose() {
        var roots = graph.findRoots();
        System.out.println("| decompose: " + roots.size() + " roots = " + roots);
        return decomposeFrom(roots);
    }

    public Map<Set<V>, DAGraph<V>> decomposeFrom(List<V> roots) {
        // Paint every vertex with the 'color' of its root.
        var colors = new HashMap<V, V>();
        for (V root : roots) {
            paintFrom(root, root, colors);
        }
        System.out.println("| decomposeFrom: " + roots.size() + " roots: colors=" + colors);

        // Traverse the graph, detecting mis-colorings:
        // If the color of a vertex differs form the color of its root, then it has been re-painted from another root.
        // It means, that sub-graphs, starting at these roots, are connected.
        // Will track this connectedness in the 'parent' hash map.
        var parents = new HashMap<V, V>();
        for (V root : roots) {
            parents.put(root, root);
        }
        for (V root : roots) {
            checkColorFrom(root, root, colors, parents);
        }

        System.out.println("| decomposeFrom: parents");
        parents.forEach((key, value) -> System.out.println("  key=" + key + "\tvalue=" + value));
        System.out.println("| decomposeFrom...");

        // "Union set find"
        var realParents = new HashMap<V, V>();
        roots.forEach(v -> {
            var curN = v;
            while (true) {
                var parent = parents.get(curN);
                if (parent == curN) break;
                curN = parent;
            }
            realParents.put(v, curN);
        });

        System.out.println("| decomposeFrom: realParents");
        realParents.forEach((key, value) -> System.out.println("  key=" + key + "\tvalue=" + value));
        System.out.println("| decomposeFrom...");

        // Invert 'parents' to produce 'rootGroups'
        var rootGroups = realParents.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toSet())));

        // Create sub-graphs for each root group
        System.out.println("| decomposeFrom " + rootGroups.size() + " rootGroups");
        var result = new HashMap<Set<V>, DAGraph<V>>();
        for (Set<V> rootGroup : rootGroups.values()) {
            System.out.println("| decomposeFrom: rootGroup=" + rootGroup);

            var subGraph = subGraphFactory.get();
            for (V root : rootGroup) {
                for (V nextVertex : graph.followers(root)) {
                    fillSubGraphFrom(nextVertex, subGraph);
                }
            }
            result.put(rootGroup, subGraph);
        }

        return result;
    }

    private void paintFrom(V vertex, V color, HashMap<V, V> colors) {
        for (V follower : graph.followers(vertex)) {
            colors.put(follower, color);
            paintFrom(follower, color, colors);
        }
    }

    private void checkColorFrom(V vertex, V color, HashMap<V, V> colors, HashMap<V, V> parents) {
        for (V follower : graph.followers(vertex)) {
            var aColor = colors.get(follower);
            System.out.println("  | " + vertex + "[color:" + color + "] ->" + follower + "[color: " + aColor + "]");
            if (!color.equals(aColor)) {
                parents.put(color, aColor);
//                parents.put(aColor, color);
            }
            checkColorFrom(follower, color, colors, parents);
        }
    }

    void fillSubGraphFrom(V vertex, DAGraph<V> sink) {
        sink.addVertex(vertex);
        for (V follower : graph.followers(vertex)) {
            sink.addEdge(vertex, follower);
            fillSubGraphFrom(follower, sink);
        }
    }
}
